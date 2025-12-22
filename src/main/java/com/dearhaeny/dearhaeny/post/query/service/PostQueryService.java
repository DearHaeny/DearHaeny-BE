package com.dearhaeny.dearhaeny.post.query.service;

import com.dearhaeny.dearhaeny.global.api.code.ErrorStatus;
import com.dearhaeny.dearhaeny.global.api.exception.GeneralException;
import com.dearhaeny.dearhaeny.post.domain.Post;
import com.dearhaeny.dearhaeny.post.domain.PostType;
import com.dearhaeny.dearhaeny.post.query.dto.PostDetailResponse;
import com.dearhaeny.dearhaeny.post.query.dto.PostListResponse;
import com.dearhaeny.dearhaeny.post.query.dto.PostListResultResponse;
import com.dearhaeny.dearhaeny.post.repository.PostRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class PostQueryService {

    private final PostRepository postRepository;

    public PostListResultResponse getPostList(
            String writerUuid,
            PostType postType,
            int page,
            int size
    ) {
        Pageable pageable = PageRequest.of(
                page,
                size,
                Sort.by(Sort.Direction.DESC, "createdAt")
        );

        Page<Post> postPage;

        // ✅ 전체 조회
        if (postType == null) {
            postPage = postRepository.findAllByWriterUuid(writerUuid, pageable);
        } 
        // ✅ 카테고리 필터
        else {
            postPage = postRepository.findAllByWriterUuidAndPostType(writerUuid, postType, pageable);
        }

        List<PostListResponse> posts = postPage.getContent().stream()
                .map(PostListResponse::from)
                .toList();

        return new PostListResultResponse(
                writerUuid,
                postType == null ? "ALL" : postType.name(),
                postPage.getTotalElements(),   // ⭐ 결과 요약 count
                postPage.getNumber(),
                postPage.getSize(),
                postPage.hasNext(),            // ⭐ 무한 스크롤 핵심
                posts
        );
    }
    public PostDetailResponse getPostDetail(Long postId, String writerUuid) {
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new IllegalArgumentException("게시글이 존재하지 않습니다."));

        // 작성자 본인 확인
        if (!post.getWriterUuid().equals(writerUuid)) {
            throw new GeneralException(ErrorStatus.INVALID_WRITER, "본인이 작성한 글에 대해서만 게시물을 조회할 수 있습니다.");
        }

        return PostDetailResponse.from(post);
    }
}

