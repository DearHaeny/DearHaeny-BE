package com.dearhaeny.dearhaeny.post.dto.response;

import com.dearhaeny.dearhaeny.post.domain.PostType;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Builder
@AllArgsConstructor
public class PostCreatedResponse {

    private Long postId;
    private PostType postType;
    private String writerUuid;

    @JsonFormat(shape=JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss'Z'", timezone = "Asia/Seoul")
    private LocalDateTime createdAt;
}
