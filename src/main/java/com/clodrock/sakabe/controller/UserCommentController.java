package com.clodrock.sakabe.controller;

import com.clodrock.sakabe.model.UserCommentResponse;
import com.clodrock.sakabe.service.CommentService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/comment")
@RequiredArgsConstructor
@CrossOrigin(origins = "http://localhost:4200")
public class UserCommentController {
    private final CommentService commentService;

    @GetMapping("/{boardId}")
    public List<UserCommentResponse> getAllByBoardId(@PathVariable("boardId") UUID boardId) {
        return commentService.getAllByBoardId(boardId);
    }

    @GetMapping
    public List<UserCommentResponse> getAll() {
        return commentService.getAll();
    }
}
