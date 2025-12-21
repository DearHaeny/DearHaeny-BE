package com.dearhaeny.dearhaeny.reply.controller;

import com.dearhaeny.dearhaeny.reply.dto.response.ReplyCreatedResponse;
import com.dearhaeny.dearhaeny.reply.service.ReplyService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/posts")
public class ReplyQueryController {

    private final ReplyService replyService;

    @GetMapping("/{postId}/reply")
    public ReplyCreatedResponse getReply(
            @PathVariable Long postId
    ) {
        return replyService.getReplyByPostId(postId);
    }
}
