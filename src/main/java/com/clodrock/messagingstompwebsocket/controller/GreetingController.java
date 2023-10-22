package com.clodrock.messagingstompwebsocket.controller;

import com.clodrock.messagingstompwebsocket.model.UserCommentResponse;
import com.clodrock.messagingstompwebsocket.model.UserCommentSaveRequest;
import com.clodrock.messagingstompwebsocket.model.UserCommentUpdateRequest;
import com.clodrock.messagingstompwebsocket.service.CommentService;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;

@Controller
@RequiredArgsConstructor
public class GreetingController {
    private final CommentService commentService;

    @MessageMapping("/send-comment")
    @SendTo("/topic/comment")
    public UserCommentResponse sendPositive(UserCommentSaveRequest request) {
        return commentService.saveComment(request);
    }

    @MessageMapping("/delete")
    @SendTo("/topic/delete")
    public void deleteComment(Long commentId) {
        commentService.deleteById(commentId);
    }

    @MessageMapping("/update")
    @SendTo("/topic/update")
    public void updateComment(UserCommentUpdateRequest updateRequest) {
        commentService.update(updateRequest);
    }
}
