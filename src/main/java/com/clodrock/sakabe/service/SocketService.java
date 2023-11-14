package com.clodrock.sakabe.service;

import com.clodrock.sakabe.model.UserCommentResponse;
import com.clodrock.sakabe.model.UserCommentSaveRequest;
import com.clodrock.sakabe.model.UserCommentUpdateRequest;
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
        socketIoServer.addEventListener("send_message", UserCommentSaveRequest.class, onCommentReceived());
        socketIoServer.addEventListener("delete_message", Long.class, onCommentDeleted());
        socketIoServer.addEventListener("update_message", UserCommentUpdateRequest.class, onCommentUpdated());
    }

    private DataListener<UserCommentSaveRequest> onCommentReceived() {
        return (senderClient, data, ackSender) -> {
            UserCommentResponse userCommentResponse = commentService.saveComment(
                    UserCommentSaveRequest
                            .builder()
                            .commentType(data.getCommentType())
                            .content(data.getContent())
                            .userId(data.getUserId())
                            .build());
            data.setId(userCommentResponse.getId());
            senderClient.getNamespace().getBroadcastOperations().sendEvent("get_message", data);
        };
    }

    private DataListener<Long> onCommentDeleted() {
        return (senderClient, data, ackSender) -> {
            senderClient.getNamespace().getBroadcastOperations().sendEvent("deleted_message", data);
            commentService.deleteById(data);
        };
    }

    private DataListener<UserCommentUpdateRequest> onCommentUpdated() {
        return (senderClient, data, ackSender) -> {
            senderClient.getNamespace().getBroadcastOperations().sendEvent("updated_message", data);
            commentService.update(
                    UserCommentUpdateRequest
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