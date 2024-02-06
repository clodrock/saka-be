package com.clodrock.sakabe.service;

import com.clodrock.sakabe.entity.UserComment;
import com.clodrock.sakabe.exception.NotFoundException;
import com.clodrock.sakabe.mapper.CommentMapper;
import com.clodrock.sakabe.model.SubCommentRequest;
import com.clodrock.sakabe.model.UserCommentResponse;
import com.clodrock.sakabe.model.UserCommentRequest;
import com.clodrock.sakabe.repository.CommentRepository;
import com.corundumstudio.socketio.SocketIOClient;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class CommentService {
    private final CommentRepository repository;
    private final CommentMapper mapper;

    public UserCommentResponse saveComment(UserCommentRequest comment) {

        UserComment save = repository.save(mapper.toComment(comment));
        return mapper.toUserComment(save);
    }

    public List<UserCommentResponse> getAllByBoardId(UUID boardId) {
        List<UserComment> comments = repository.findAllByBoardIdAndParentIdIsNull(boardId);
        List<UserComment> subComments = repository.findAllByBoardIdAndParentIdIsNotNull(boardId);

        if(comments.isEmpty())
            throw new NotFoundException("No comment has been found!");

        return comments.stream().map(p-> {
            List<UserCommentResponse> subs = subComments.stream().filter(sub -> sub.getParentId().equals(p.getId()))
                    .map(mapper::toUserComment).toList();
            return mapper.toUserComment(p, subs);
        }).toList();
    }

    public List<UserCommentResponse> getAll() {
        List<UserComment> comments = repository.findAll();

        if(comments.isEmpty())
            throw new NotFoundException("No comment has been found!");

        return comments.stream().map(mapper::toUserComment).toList();
    }

    public void deleteById(Long commentId) {
        repository.deleteById(commentId);
    }

    public void update(UserCommentRequest updateRequest) {
        Optional<UserComment> comment = repository.findById(updateRequest.getId());
        comment.ifPresent(c-> {
            c.setCommentType(updateRequest.getCommentType());
            c.setContent(updateRequest.getContent());
            repository.save(c);
        });
    }

    public Optional<UserCommentResponse> findById(Long id) {
        Optional<UserComment> comment = repository.findById(id);
        return comment.map(mapper::toUserComment);
    }

    public void saveSubComment(SubCommentRequest comment) {
        repository.save(mapper.fromSubComment(comment));
    }
}
