package com.clodrock.sakabe.repository;

import com.clodrock.sakabe.entity.UserComment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface CommentRepository extends JpaRepository<UserComment, Long> {
    List<UserComment> findAllByBoardIdAndParentIdIsNull(UUID boardId);

    List<UserComment> findAllByBoardIdAndParentIdIsNotNull(UUID boardId);
}
