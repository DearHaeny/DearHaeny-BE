package com.dearhaeny.dearhaeny.post.dto.request;

import com.dearhaeny.dearhaeny.post.domain.PostType;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PostCreateRequest {

    private String writerUuid;
    private String nickname;
    private PostType postType;
    private String content;
}