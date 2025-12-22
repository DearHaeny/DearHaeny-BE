package com.dearhaeny.dearhaeny.reply.controller;

import com.dearhaeny.dearhaeny.reply.dto.response.ReplyCreatedResponse;
import com.dearhaeny.dearhaeny.reply.service.ReplyService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/posts")
public class ReplyQueryController {

    private final ReplyService replyService;

    @GetMapping("/{postId}/reply")
    public ReplyCreatedResponse getReply(
            HttpServletRequest request,
            @PathVariable Long postId
    ) {

        // uuid 추출
        String writerUuid = (String) request.getAttribute("anonId");
        return replyService.getReplyByPostId(postId, writerUuid);
    }
}
