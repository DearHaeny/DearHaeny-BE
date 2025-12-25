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
            HttpServletRequest request,                         // 인터셉터에서 넘겨준 anonId를 받기 위함
            @RequestParam(required = false) PostType category, // 전체면 null
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {

        // uuid 꺼내기
        String writerUuid = (String) request.getAttribute("anonId");

        return postQueryService.getPostList(writerUuid, category, page, size);
    }
    @GetMapping("/{postId}")
    public PostDetailResponse getPostDetail(
            @RequestHeader("anonId") String writerUuid,
            @PathVariable Long postId
    ) {

        return postQueryService.getPostDetail(postId, writerUuid);
    }

}
