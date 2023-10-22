package com.clodrock.messagingstompwebsocket.repository;

import com.clodrock.messagingstompwebsocket.entity.Comment;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CommentRepository extends JpaRepository<Comment, Long> {
}
