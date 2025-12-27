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

    public PostListResultResponse getPostList(PostType postType) {

        // 최신순 정렬
        Sort sort = Sort.by(Sort.Direction.DESC, "createdAt");

        List<Post> postList;

        // ✅ 전체 조회
        if (postType == null) {
            postList = postRepository.findAll(sort);
        } 
        // ✅ 카테고리 필터
        else {
            postList = postRepository.findAllByPostType(postType, sort);
        }

        List<PostListResponse> posts = postList.stream()
                .map(PostListResponse::from)
                .toList();

        return new PostListResultResponse(
                postType == null ? "ALL" : postType.name(),
                (long) posts.size(),            // 전체 개수
                posts
        );
    }
    public PostDetailResponse getPostDetail(String writerUuid, Long postId) {
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new IllegalArgumentException("게시글이 존재하지 않습니다."));

        return PostDetailResponse.from(post);
    }
}

