package com.dearhaeny.dearhaeny.post.service;

import com.dearhaeny.dearhaeny.global.api.code.ErrorStatus;
import com.dearhaeny.dearhaeny.global.api.exception.GeneralException;
import com.dearhaeny.dearhaeny.post.domain.Post;
import com.dearhaeny.dearhaeny.post.dto.request.PostCreateRequest;
import com.dearhaeny.dearhaeny.post.dto.response.PostCreatedResponse;
import com.dearhaeny.dearhaeny.post.repository.PostRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@Slf4j
@RequiredArgsConstructor
public class PostService {

    private final PostRepository postRepository;

    // 마음 글 작성하기
    @Transactional
    public PostCreatedResponse sendPost(PostCreateRequest request, String writerUuid) {

        // 게시글을 작성하기 위해서는 모든 필드가 작성돼 있어야 한다
        if (request.getNickname() == null || request.getNickname().isBlank()) {
            throw new GeneralException(ErrorStatus.VALIDATION_ERROR, "닉네임을 입력해 주세요.");
        }

        // 사용하는 브라우저 내에서만 닉네임 중복을 검증
        if (postRepository.existsByNicknameAndWriterUuid(request.getNickname(), writerUuid)) {
            throw new GeneralException(ErrorStatus.DUPLICATE_NICKNAME);
        }

        if (request.getContent() == null || request.getContent().isBlank()) {
            throw new GeneralException(ErrorStatus.VALIDATION_ERROR, "게시글을 작성해 주세요.");
        }

        if (request.getPostType() == null) {
            throw new GeneralException(ErrorStatus.VALIDATION_ERROR, "게시글 타입을 선택해 주세요.");
        }

        try {
            // Post 엔티티 생성
            Post post = Post.builder()
                    .writerUuid(writerUuid)
                    .nickname(request.getNickname())
                    .postType(request.getPostType())
                    .content(request.getContent())
                    .build();

            // 저장
            // 닉네임이 중복일 경우 DataIntegrityViolationException 발생
            Post savedPost = postRepository.saveAndFlush(post);

            // responseDto 반환
            return PostCreatedResponse.builder()
                    .postId(savedPost.getPostId())
                    .postType(savedPost.getPostType())
                    .writerUuid(savedPost.getWriterUuid())
                    .createdAt(post.getCreatedAt())
                    .build();
        } catch (Exception e) {
            // 메시지에 중복이 포함되어 있다면 GeneralException으로 변환
            if (e.getMessage().contains("Duplicate entry")) {
                throw new GeneralException(ErrorStatus.DUPLICATE_NICKNAME);
            }
            throw e;
        }
    }

}
