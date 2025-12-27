package com.dearhaeny.dearhaeny.post.query.controller;

import com.dearhaeny.dearhaeny.post.domain.PostType;
import com.dearhaeny.dearhaeny.post.query.dto.PostDetailResponse;
import com.dearhaeny.dearhaeny.post.query.dto.PostListResultResponse;
import com.dearhaeny.dearhaeny.post.query.service.PostQueryService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/posts")
public class PostQueryController {

    private final PostQueryService postQueryService;

    @GetMapping
    public PostListResultResponse getPosts(
            @RequestHeader(value = "anonId", required = false) String writerUuid,
            @RequestParam(required = false) PostType category, // 전체면 null
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {

        return postQueryService.getPostList(category);
    }
    @GetMapping("/{postId}")
    public PostDetailResponse getPostDetail(
            @RequestHeader(value = "anonId", required = false) String writerUuid,
            @PathVariable Long postId
    ) {

        return postQueryService.getPostDetail(writerUuid, postId);
    }

}
