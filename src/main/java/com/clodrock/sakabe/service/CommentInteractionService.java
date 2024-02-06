package com.clodrock.sakabe.service;

import com.clodrock.sakabe.entity.Interaction;
import com.clodrock.sakabe.enums.CommentInteractionType;
import com.clodrock.sakabe.model.EmojiRequest;
import com.clodrock.sakabe.repository.CommentInteractionRepository;
import com.clodrock.sakabe.repository.CommentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import com.clodrock.sakabe.entity.UserComment;

import java.util.Optional;
import java.util.UUID;

@RequiredArgsConstructor
@Service
public class CommentInteractionService {
    private final CommentInteractionRepository interactionRepository;
    private final CommentRepository commentRepository;

    public void save(EmojiRequest request, UUID boardId) {
        Optional<UserComment> comment = commentRepository.findById(request.commentId());
        if(comment.isPresent()){
            Interaction interaction = interactionRepository.findByBoardIdAndUserIdAndUserComment(boardId, request.userId(), comment.get()).map(p -> {
                p.setCount(request.interactionType().equals(CommentInteractionType.NEGATIVE) ? p.getCount() -1 : p.getCount() + 1);
                return p;
            }).orElseGet(() -> Interaction.builder()
                    .emojiCode(request.emojiCode())
                    .boardId(boardId)
                    .userComment(comment.get())
                    .userId(request.userId())
                    .count(request.interactionType().equals(CommentInteractionType.NEGATIVE) ? -1 : 1)
                    .build());

            if(interaction.getCount() == 0) {
                interactionRepository.delete(interaction);
            }else{
                interactionRepository.save(interaction);
            }
        }
    }
}
