package com.dearhaeny.dearhaeny.post.repository;

import com.dearhaeny.dearhaeny.post.domain.Post;
import com.dearhaeny.dearhaeny.post.domain.PostType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PostRepository extends JpaRepository<Post, Long> {

    boolean existsByNicknameAndWriterUuid(String nickname, String writerUuid);

    // 목록 조회
    Page<Post> findAllByWriterUuid(String writerUuid, Pageable pageable);
    Page<Post> findAllByWriterUuidAndPostType(String writerUuid, PostType postType, Pageable pageable);

    List<Post> findAll(Sort sort);
    List<Post> findAllByPostType(PostType postType, Sort sort);

}
