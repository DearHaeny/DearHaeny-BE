package com.dearhaeny.dearhaeny.reply.repository;

import com.dearhaeny.dearhaeny.reply.domain.Reply;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ReplyRepository extends JpaRepository<Reply, Long> {
    Optional<Reply> findByPost_PostId(Long postId);
}
