package com.dearhaeny.dearhaeny.post.controller;

import com.dearhaeny.dearhaeny.global.api.dto.ApiResponse;
import com.dearhaeny.dearhaeny.post.domain.Post;
import com.dearhaeny.dearhaeny.post.dto.request.PostCreateRequest;
import com.dearhaeny.dearhaeny.post.dto.response.PostCreatedResponse;
import com.dearhaeny.dearhaeny.post.service.PostService;
import com.dearhaeny.dearhaeny.reply.dto.response.ReplyCreatedResponse;
import com.dearhaeny.dearhaeny.reply.service.ReplyService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/posts")
@Slf4j
@RequiredArgsConstructor
public class PostController {

    private final PostService postService;
    private final ReplyService replyService;

    @PostMapping
    public ApiResponse<PostCreatedResponse> postCreate(
            @RequestBody PostCreateRequest request
    ) {
        PostCreatedResponse response = postService.sendPost(request);
        return ApiResponse.success(response);
    }

    @PostMapping("/{postId}/reply")
    public ApiResponse<ReplyCreatedResponse> replyCreate(
            @PathVariable Long postId
    ) {
        return ApiResponse.success(replyService.createReply(postId));
    }
}
