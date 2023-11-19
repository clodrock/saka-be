package com.clodrock.sakabe.service;

import com.clodrock.sakabe.model.UserCommentRequest;
import com.clodrock.sakabe.model.UserCommentResponse;
import com.corundumstudio.socketio.SocketIOClient;
import com.corundumstudio.socketio.SocketIOServer;
import com.corundumstudio.socketio.listener.ConnectListener;
import com.corundumstudio.socketio.listener.DataListener;
import com.corundumstudio.socketio.listener.DisconnectListener;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.Optional;
import java.util.stream.Collectors;

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

    public void sendSocketMessage(SocketIOClient senderClient, UserCommentRequest userCommentRequest, String streamType) {
        for (
                SocketIOClient client : senderClient.getNamespace().getRoomOperations(userCommentRequest.getBoardId()).getClients()) {
            if (!client.getSessionId().equals(senderClient.getSessionId())) {
                client.sendEvent(streamType, userCommentRequest);
            }
        }
    }

    private DataListener<UserCommentRequest> onCommentReceived() {
        return (senderClient, data, ackSender) -> {
            Optional<String> isMemberOfBoard = senderClient.getAllRooms().stream().filter(p -> p.equals(data.getBoardId())).findFirst();

            UserCommentResponse userCommentResponse = commentService.saveComment(
                    senderClient,
                    UserCommentRequest
                            .builder()
                            .commentType(data.getCommentType())
                            .content(data.getContent())
                            .userId(data.getUserId())
                            .build());
            data.setId(userCommentResponse.getId());
            sendSocketMessage(senderClient,data, "get_message");
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
//            String room = client.getHandshakeData().getSingleUrlParam("room");
//            String username = client.getHandshakeData().getSingleUrlParam("room");
            var params = client.getHandshakeData().getUrlParams();
            String room = String.join("", params.get("room"));
            String username = String.join("", params.get("username"));
            client.joinRoom(room);
            log.info("Socket ID[{}] - room[{}] - username [{}]  Connected to chat module through", client.getSessionId().toString(), room, username);
        };

    }

    private DisconnectListener onDisconnected() {
        return client -> {
            var params = client.getHandshakeData().getUrlParams();
            String room = String.join("", params.get("room"));
            String username = String.join("", params.get("username"));
            log.info("Socket ID[{}] - room[{}] - username [{}]  discnnected to chat module through", client.getSessionId().toString(), room, username);
        };
    }

}