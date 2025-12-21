package com.dearhaeny.dearhaeny.post.query.dto;

import com.dearhaeny.dearhaeny.post.domain.Post;
import java.time.LocalDateTime;

public record PostDetailResponse(
        Long postId,
        String nickname,
        String postType,
        LocalDateTime createdAt,
        String content
) {
    public static PostDetailResponse from(Post post) {
        return new PostDetailResponse(
                post.getPostId(),
                post.getNickname(),
                post.getPostType().name(),
                post.getCreatedAt(),
                post.getContent()
        );
    }
}
