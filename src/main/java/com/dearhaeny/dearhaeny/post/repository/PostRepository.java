package com.dearhaeny.dearhaeny.post.repository;

import com.dearhaeny.dearhaeny.post.domain.Post;
import com.dearhaeny.dearhaeny.post.domain.PostType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PostRepository extends JpaRepository<Post, Long> {

    boolean existsByNickname(String nickname);

    Page<Post> findAllByPostType(PostType postType, Pageable pageable);
    
}
