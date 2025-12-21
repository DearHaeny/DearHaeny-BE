package com.dearhaeny.dearhaeny.post.query.dto;

import com.dearhaeny.dearhaeny.post.domain.Post;
import java.time.LocalDateTime;

public record PostListResponse(
    Long postId,
    String nickname,
    String content,
    String postType,
    LocalDateTime createdAt
) {
public static PostListResponse from(Post post) {
    return new PostListResponse(
            post.getPostId(),          // postId
            post.getNickname(),        // 작성자
            post.getContent(),         // 카드에 보여줄 메시지
            post.getPostType().name(), // 카테고리
            post.getCreatedAt()        // 생성 시간
    );
}
}