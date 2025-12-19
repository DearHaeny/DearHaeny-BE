package com.dearhaeny.dearhaeny.reply.service;

import com.dearhaeny.dearhaeny.global.api.code.ErrorStatus;
import com.dearhaeny.dearhaeny.global.api.exception.GeneralException;
import com.dearhaeny.dearhaeny.global.gemini.GeminiService;
import com.dearhaeny.dearhaeny.post.domain.Post;
import com.dearhaeny.dearhaeny.post.repository.PostRepository;
import com.dearhaeny.dearhaeny.reply.domain.Reply;
import com.dearhaeny.dearhaeny.reply.domain.ReplyStatus;
import com.dearhaeny.dearhaeny.reply.dto.response.ReplyCreatedResponse;
import com.dearhaeny.dearhaeny.reply.repository.ReplyRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import java.util.Optional;

import static org.springframework.util.StringUtils.hasText;

@Service
@Slf4j
@RequiredArgsConstructor
public class ReplyService {

    private final PostRepository postRepository;
    private final ReplyRepository replyRepository;
    private final GeminiService geminiService;

    // postId를 기반으로 Reply를 생성한다.
    @Transactional
    public ReplyCreatedResponse createReply(Long postId) {

        // post(마음 게시글) 존재 확인
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new GeneralException(ErrorStatus.POST_NOT_FOUND));

        // Post–Reply는 1:1 관계
        // → 이미 생성된 reply가 있으면 재생성하지 않고 그대로 반환
        Optional<Reply> existingReply = replyRepository.findByPost_PostId(postId);
        if (existingReply.isPresent()) {
            log.info("Reply with postId {} already exists", postId);
            return toResponse(existingReply.get());
        }

        Reply reply = Reply.builder()
                .post(post)
                .content(null)                          // Ai 응답 생성 전이므로 content는 Null로 저장
                .status(ReplyStatus.GENERATING)
                .createdAt(post.getCreatedAt())         // post 전송 시각을 기준
                .build();

        try {
            reply = replyRepository.saveAndFlush(reply);

            // 프롬프트 구성
            String prompt = buildPrompt(post);

            // Gemini 호출 (1회만, 재시도 X)
            String aiText = geminiService.callGemini(prompt);

            if (!hasText(aiText)) {
                throw new GeneralException(ErrorStatus.GEMINI_EMPTY_TEXT);
            }

            // 성공 처리
            reply.setContent(aiText.trim());
            reply.setStatus(ReplyStatus.COMPLETED);

            return toResponse(reply);
        } catch (DataIntegrityViolationException e) {
            // 동시 요청으로 인한 post_id의 UNIQUE 제약이 충돌할 경우
            Reply existing = replyRepository.findByPost_PostId(postId)
                    .orElseThrow(() -> new GeneralException(ErrorStatus.REPLY_ALREADY_EXIST));
            return toResponse(existing);
        } catch (GeneralException e) {
            // Gemini 호출 실패/차단/포맷 오류 등: FAILED 상태로 저장
            reply.setStatus(ReplyStatus.FAILED);
            throw e;
        } catch (Exception e) {
            reply.setStatus(ReplyStatus.FAILED);
            throw new GeneralException(ErrorStatus.INTERNAL_SERVER_ERROR, e.getMessage());
        }
    }

    private ReplyCreatedResponse toResponse(Reply reply) {
        return ReplyCreatedResponse.builder()
                .replyId(reply.getReplyId())
                .content(reply.getContent())
                .replyStatus(reply.getStatus())
                .createdAt(reply.getCreatedAt())
                .build();
    }

    private String buildPrompt(Post post) {
        return """
                당신은 사용자의 마음 글에 답장해 주는 따뜻한 조력자 ‘해니’입니다.

            [입력 변수]
            - nickname: "%s"
            - postType: "%s"
            - content: "%s"

            [최우선 공통 규칙]
            1) 답장 첫 문장은 반드시 "%s님," 으로 시작한다.
            2) 존댓말 + 따뜻하고 배려 깊은 톤으로, ‘해니’ 말투로 작성한다.
            3) 본인을 지칭할 때는 항상 "해니"라고 표현한다.
            4) 답장 길이는 300자 이내로 제한한다.
            5) 답장 마지막 문장은 반드시 따뜻한 응원의 문장으로 마친다.

            [핵심: 내용 근거 규칙]
            - 답장은 반드시 위 content에 포함된 사실/감정/목표만 근거로 작성한다,
            - content에 없는 취미, 직업, 상황(예: 그림 배우기, 회사, 시험 등)을 절대 추가로 만들어내지 않는다.
            - 사용자가 말하지 않은 구체 목표/계획을 임의로 추측하지 않는다.
            - 필요하면 content에서 드러난 표현을 바꾸어(요약/재진술) 공감한다.

            [감정 칩별 규칙: postType을 기준으로 구분]
            - postType이 "NEW_YEAR_WISH" 인 경우:
              * 사용자의 새해소원을 진심으로 응원한다.
              * 목표를 이루는 데 도움이 될 해니의 "간단한 조언 한 줄"을 포함한다.
              * 희망적이고 밝은 분위기 유지.
              * 참고: 내년은 2026년이다.

            - postType이 "INNER_MIND" 인 경우:
              * 감정을 충분히 공감하고 위로한다.
              * 판단/강요 금지, 부담 없는 부드러운 지지.

            - postType이 "COURAGE" 인 경우:
              * 용기를 북돋고 한 걸음 나아가도록 힘을 실어준다.
              * 긍정적이고 든든한 분위기 유지.

            [출력 규칙]
            - 한국어 답장 1개만 출력한다.
            - 제목/설명/분석/불릿/규칙 재출력 없이, 답장 본문만 출력한다.
            """.formatted(
                post.getNickname(),      // nickname
                post.getPostType(),      // postType
                post.getContent(),       // content
                post.getNickname()       // "%s님,"
        );
    }

    private static class ReturnExistingReply extends RuntimeException {
        private final Reply reply;
        private ReturnExistingReply(Reply reply) {
            this.reply = reply;
        }
    }

}
