package com.clodrock.sakabe.service;

import com.clodrock.sakabe.entity.Comment;
import com.clodrock.sakabe.mapper.CommentMapper;
import com.clodrock.sakabe.model.UserCommentResponse;
import com.clodrock.sakabe.model.UserCommentSaveRequest;
import com.clodrock.sakabe.model.UserCommentUpdateRequest;
import com.clodrock.sakabe.repository.CommentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CommentService {
    private final CommentRepository repository;
    private final CommentMapper mapper;

    public UserCommentResponse saveComment(UserCommentSaveRequest comment) {
        Comment save = repository.save(mapper.toComment(comment));
        return mapper.toUserComment(save);
    }

    public List<UserCommentResponse> getAll() {
        List<Comment> all = repository.findAll();
        return all.stream().map(mapper::toUserComment).toList();
    }

    public void deleteById(Long commentId) {
        repository.deleteById(commentId);
    }

    public void update(UserCommentUpdateRequest updateRequest) {
        Optional<Comment> byId = repository.findById(updateRequest.id());
        byId.ifPresent(p-> {
            p.setCommentType(updateRequest.commentType());
            p.setContent(updateRequest.content());
            repository.save(p);
        });
    }
}
