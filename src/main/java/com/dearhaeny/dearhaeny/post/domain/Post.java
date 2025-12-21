package com.dearhaeny.dearhaeny.post.domain;

import com.dearhaeny.dearhaeny.reply.domain.Reply;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Post {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="post_id",nullable = false, unique = true)
    private Long postId;

    @Column(name="writer_uuid", nullable = false)
    private String writerUuid;          // 작성자 식별

    @Column(name="nickname", nullable = false, unique = true)
    private String nickname;

    @Enumerated(EnumType.STRING)
    @Column(name="postType", nullable = false)
    private PostType postType;

    @Column(name="content", nullable = false, length = 100)
    private String content;

    @CreationTimestamp
    @Column(name="createdAt", updatable = false)
    private LocalDateTime createdAt;

    // Post : Reply = 1 : 1
    @OneToOne(mappedBy = "post", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private Reply reply;

}
