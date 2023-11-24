package com.clodrock.sakabe.repository;

import com.clodrock.sakabe.entity.Comment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CommentRepository extends JpaRepository<Comment, Long> {
    List<Comment> findAllByBoardId(String boardId);
}
