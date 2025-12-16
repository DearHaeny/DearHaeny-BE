package com.dearhaeny.dearhaeny.post.domain;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

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

}
