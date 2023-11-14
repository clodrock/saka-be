package com.clodrock.sakabe.controller;

import com.clodrock.sakabe.model.UserCommentResponse;
import com.clodrock.sakabe.service.CommentService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/comment")
@RequiredArgsConstructor
public class CommentController {
    private final CommentService commentService;

    @GetMapping
    public List<UserCommentResponse> getAll() {
        return commentService.getAll();
    }
}
