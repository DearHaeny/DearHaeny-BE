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

@Service
@Slf4j
@RequiredArgsConstructor
public class PostService {

    private final PostRepository postRepository;

    // 마음 글 작성하기
    @Transactional
    public PostCreatedResponse sendPost(PostCreateRequest request) {

        // 게시글을 작성하기 위해서는 모든 필드가 작성돼 있어야 한다

        if (request.getNickname() == null || request.getNickname().isBlank()) {
            throw new GeneralException(ErrorStatus.VALIDATION_ERROR, "닉네임을 입력해 주세요.");
        }

        if (request.getContent() == null || request.getContent().isBlank()) {
            throw new GeneralException(ErrorStatus.VALIDATION_ERROR, "게시글을 작성해 주세요.");
        }

        if (request.getPostType() == null) {
            throw new GeneralException(ErrorStatus.VALIDATION_ERROR, "게시글 타입을 선택해 주세요.");
        }

        // Post 엔티티 생성
        Post post = Post.builder()
                .nickname(request.getNickname())
                .postType(request.getPostType())
                .content(request.getContent())
                .build();

        // 저장
        Post savedPost = postRepository.save(post);

        // responseDto 반환
        return PostCreatedResponse.builder()
                .postId(savedPost.getPostId())
                .postType(savedPost.getPostType())
                .createdAt(post.getCreatedAt())
                .build();
    }
}
