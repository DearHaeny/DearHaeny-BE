package com.dearhaeny.dearhaeny.post.query.service;

import com.dearhaeny.dearhaeny.post.domain.Post;
import com.dearhaeny.dearhaeny.post.domain.PostType;
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
            postPage = postRepository.findAll(pageable);
        } 
        // ✅ 카테고리 필터
        else {
            postPage = postRepository.findAllByPostType(postType, pageable);
        }

        List<PostListResponse> posts = postPage.getContent().stream()
                .map(PostListResponse::from)
                .toList();

        return new PostListResultResponse(
                postType == null ? "ALL" : postType.name(),
                postPage.getTotalElements(),   // ⭐ 결과 요약 count
                postPage.getNumber(),
                postPage.getSize(),
                postPage.hasNext(),            // ⭐ 무한 스크롤 핵심
                posts
        );
    }
}

