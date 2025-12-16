package com.dearhaeny.dearhaeny.post.dto.response;

import com.dearhaeny.dearhaeny.post.domain.PostType;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@Builder
@AllArgsConstructor
public class PostCreatedResponse {

    private Long postId;
    private PostType postType;

    @JsonFormat(shape=JsonFormat.Shape.STRING, pattern = "yyyy-mm-dd'T'HH:mm:ss'Z'", timezone = "Asia/Seoul")
    private LocalDateTime createdAt;
}
