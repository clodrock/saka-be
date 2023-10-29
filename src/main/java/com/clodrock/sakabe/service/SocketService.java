package com.clodrock.sakabe.service;

import com.clodrock.sakabe.model.UserCommentSaveRequest;
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
        socketIoServer.addEventListener("send_message", UserCommentSaveRequest.class, onChatReceived());
    }

    private DataListener<UserCommentSaveRequest> onChatReceived() {
        return (senderClient, data, ackSender) -> {
            senderClient.getNamespace().getBroadcastOperations().sendEvent("get_message", data);
            commentService.saveComment(
                    UserCommentSaveRequest
                            .builder()
                            .commentType(data.getCommentType())
                            .content(data.getContent())
                            .userId(data.getUserId())
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