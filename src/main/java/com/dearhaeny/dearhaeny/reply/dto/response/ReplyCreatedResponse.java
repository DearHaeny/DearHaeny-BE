package com.dearhaeny.dearhaeny.reply.dto.response;

import com.dearhaeny.dearhaeny.reply.domain.ReplyStatus;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@Builder
public class ReplyCreatedResponse {

    private Long replyId;
    private String content;
    private ReplyStatus replyStatus;

    // 생성 시간은 post가 전송된 시점을 기준으로 한다.
    @JsonFormat(shape=JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss'Z'", timezone = "Asia/Seoul")
    private LocalDateTime createdAt;
}
