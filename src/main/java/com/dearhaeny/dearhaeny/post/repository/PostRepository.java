package com.dearhaeny.dearhaeny.post.repository;

import com.dearhaeny.dearhaeny.post.domain.Post;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PostRepository extends JpaRepository<Post, Long> {

    boolean existsByNickname(String nickname);
}
