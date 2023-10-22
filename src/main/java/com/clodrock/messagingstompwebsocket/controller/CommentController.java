package com.clodrock.messagingstompwebsocket.controller;

import com.clodrock.messagingstompwebsocket.model.UserCommentResponse;
import com.clodrock.messagingstompwebsocket.model.UserCommentSaveRequest;
import com.clodrock.messagingstompwebsocket.model.UserCommentUpdateRequest;
import com.clodrock.messagingstompwebsocket.service.CommentService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/comment")
@RequiredArgsConstructor
public class CommentController {
    private final CommentService commentService;

    @PostMapping
    public UserCommentResponse saveComment(@RequestBody UserCommentSaveRequest userCommentSaveRequest) {
        return commentService.saveComment(userCommentSaveRequest);
    }

    @GetMapping
    public List<UserCommentResponse> getAll() {
        return commentService.getAll();
    }

    @DeleteMapping("/{id}")
    public void deleteById(@PathVariable("id") Long id) {
        commentService.deleteById(id);
    }

    @PutMapping
    public void update(@RequestBody UserCommentUpdateRequest updateRequest) {
        commentService.update(updateRequest);
    }
}
