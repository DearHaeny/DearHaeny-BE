package com.dearhaeny.dearhaeny.post.controller;

import com.dearhaeny.dearhaeny.global.api.dto.ApiResponse;
import com.dearhaeny.dearhaeny.post.dto.request.PostCreateRequest;
import com.dearhaeny.dearhaeny.post.dto.response.PostCreatedResponse;
import com.dearhaeny.dearhaeny.post.service.PostService;
import com.dearhaeny.dearhaeny.reply.dto.response.ReplyCreatedResponse;
import com.dearhaeny.dearhaeny.reply.service.ReplyService;
import jakarta.servlet.http.HttpServletRequest;
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
            HttpServletRequest request,
            @RequestBody PostCreateRequest postCreateRequest
    ) {

        // 인터셉터에서 설정한 anonId 꺼내기
        String anonId = (String) request.getAttribute("anonId");

        PostCreatedResponse response = postService.sendPost(postCreateRequest, anonId);
        return ApiResponse.success(response);
    }

    @PostMapping("/{postId}/reply")
    public ApiResponse<ReplyCreatedResponse> replyCreate(
            @RequestHeader("anonId") String anonId,
            @PathVariable Long postId
    ) {

        log.info("요청받은 anonId: {}, postId: {}", anonId, postId);
        return ApiResponse.success(replyService.createReply(postId, anonId));
    }
}
