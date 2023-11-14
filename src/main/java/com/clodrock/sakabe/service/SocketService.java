package com.clodrock.sakabe.service;

import com.clodrock.sakabe.model.UserCommentRequest;
import com.clodrock.sakabe.model.UserCommentResponse;
import com.corundumstudio.socketio.SocketIOServer;
import com.corundumstudio.socketio.listener.ConnectListener;
import com.corundumstudio.socketio.listener.DataListener;
import com.corundumstudio.socketio.listener.DisconnectListener;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class SocketService {
    private final SocketIOServer socketIoServer;
    private final CommentService commentService;

    public SocketService(SocketIOServer socketIoServer, CommentService commentService) {
        this.socketIoServer = socketIoServer;
        this.commentService = commentService;
        socketIoServer.addConnectListener(onConnected());
        socketIoServer.addDisconnectListener(onDisconnected());
        socketIoServer.addEventListener("send_message", UserCommentRequest.class, onCommentReceived());
        socketIoServer.addEventListener("delete_message", UserCommentRequest.class, onCommentDeleted());
        socketIoServer.addEventListener("update_message", UserCommentRequest.class, onCommentUpdated());
    }

    private DataListener<UserCommentRequest> onCommentReceived() {
        return (senderClient, data, ackSender) -> {
            UserCommentResponse userCommentResponse = commentService.saveComment(
                    UserCommentRequest
                            .builder()
                            .commentType(data.getCommentType())
                            .content(data.getContent())
                            .userId(data.getUserId())
                            .build());
            data.setId(userCommentResponse.getId());
            senderClient.getNamespace().getBroadcastOperations().sendEvent("get_message", data);
        };
    }

    private DataListener<UserCommentRequest> onCommentDeleted() {
        return (senderClient, data, ackSender) -> {
            UserCommentResponse userComment = commentService.findById(data.getId());
            commentService.deleteById(data.getId());
            data.setCommentType(userComment.getCommentType());
            data.setContent(userComment.getContent());
            data.setUserId(userComment.getUserId());
            senderClient.getNamespace().getBroadcastOperations().sendEvent("deleted_message", data);
        };
    }

    private DataListener<UserCommentRequest> onCommentUpdated() {
        return (senderClient, data, ackSender) -> {
            senderClient.getNamespace().getBroadcastOperations().sendEvent("updated_message", data);
            commentService.update(
                    UserCommentRequest
                            .builder()
                            .commentType(data.getCommentType())
                            .content(data.getContent())
                            .id(data.getId())
                            .build());
        };
    }

    private ConnectListener onConnected() {
        return (client) -> {
            log.info("Socket ID[{}]  Connected to socket", client.getSessionId().toString());
        };
    }

    private DisconnectListener onDisconnected() {
        return client -> {
            log.info("Client[{}] - Disconnected from socket", client.getSessionId().toString());
        };
    }

}