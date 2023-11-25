package com.clodrock.sakabe.service;

import com.clodrock.sakabe.entity.Comment;
import com.clodrock.sakabe.exception.NotFoundException;
import com.clodrock.sakabe.mapper.CommentMapper;
import com.clodrock.sakabe.model.UserCommentResponse;
import com.clodrock.sakabe.model.UserCommentRequest;
import com.clodrock.sakabe.repository.CommentRepository;
import com.corundumstudio.socketio.SocketIOClient;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CommentService {
    private final CommentRepository repository;
    private final CommentMapper mapper;

    public UserCommentResponse saveComment(UserCommentRequest comment) {

        Comment save = repository.save(mapper.toComment(comment));
        return mapper.toUserComment(save);
    }

    public List<UserCommentResponse> getAllByBoardId(String boardId) {
        List<Comment> comments = repository.findAllByBoardId(boardId);

        if(comments.isEmpty())
            throw new NotFoundException("No comment has been found!");

        return comments.stream().map(mapper::toUserComment).toList();
    }

    public List<UserCommentResponse> getAll() {
        List<Comment> comments = repository.findAll();

        if(comments.isEmpty())
            throw new NotFoundException("No comment has been found!");

        return comments.stream().map(mapper::toUserComment).toList();
    }

    public void deleteById(Long commentId) {
        repository.deleteById(commentId);
    }

    public void update(UserCommentRequest updateRequest) {
        Optional<Comment> comment = repository.findById(updateRequest.getId());
        comment.ifPresent(c-> {
            c.setCommentType(updateRequest.getCommentType());
            c.setContent(updateRequest.getContent());
            repository.save(c);
        });
    }

    public UserCommentResponse findById(Long id) {
        Optional<Comment> comment = repository.findById(id);
        return comment.map(mapper::toUserComment).orElseThrow(()-> new NotFoundException("Comment not found!"));
    }
}
