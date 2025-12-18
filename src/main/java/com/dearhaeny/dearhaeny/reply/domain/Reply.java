package com.dearhaeny.dearhaeny.reply.domain;

import com.dearhaeny.dearhaeny.post.domain.Post;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Reply {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="reply_id", nullable = false, unique = true)
    private Long replyId;

    @OneToOne(fetch=FetchType.LAZY)
    @JoinColumn(name="post_id", nullable = false, unique = true)
    private Post post;

    @Column(name="content", columnDefinition = "TEXT", length = 300)
    private String content;

    @Enumerated(EnumType.STRING)
    @Column(name="reply_status", nullable = false)
    private ReplyStatus status;

    @Column(name="created_at", updatable = false)
    private LocalDateTime createdAt;

}
