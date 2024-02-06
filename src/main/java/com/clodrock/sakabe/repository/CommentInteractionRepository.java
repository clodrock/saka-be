package com.clodrock.sakabe.repository;

import com.clodrock.sakabe.entity.Interaction;
import com.clodrock.sakabe.entity.UserComment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface CommentInteractionRepository extends JpaRepository<Interaction, Long> {
    Optional<Interaction> findByBoardIdAndUserIdAndUserComment(UUID boardId, UUID userId, UserComment userComment);
}
