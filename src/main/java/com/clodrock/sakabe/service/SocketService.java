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

import java.util.Collection;

@Component
@Slf4j
public class SocketService {
    private final SocketIOServer socketIoServer;
    private final CommentService commentService;
    private final BoardService boardService;


    public SocketService(SocketIOServer socketIoServer, CommentService commentService, BoardService boardService, AuthenticationService authenticationService) {
        this.socketIoServer = socketIoServer;
        this.commentService = commentService;
        this.boardService = boardService;
        socketIoServer.addConnectListener(onConnected());
        socketIoServer.addDisconnectListener(onDisconnected());
        socketIoServer.addEventListener("send_message", UserCommentRequest.class, onCommentReceived());
        socketIoServer.addEventListener("delete_message", UserCommentRequest.class, onCommentDeleted());
        socketIoServer.addEventListener("update_message", UserCommentRequest.class, onCommentUpdated());
    }

    public void sendSocketMessage(SocketIOClient senderClient, UserCommentRequest userCommentRequest, String streamType, String boardId) {
        Collection<SocketIOClient> clients = senderClient.getNamespace().getRoomOperations(boardId).getClients();

        for (SocketIOClient client : clients) {
                client.sendEvent(streamType, userCommentRequest);
        }
    }

    private DataListener<UserCommentRequest> onCommentReceived() {
        return (senderClient, data, ackSender) -> {
            var userCommentResponse = commentService.saveComment(data);
            data.setId(userCommentResponse.getId());
            sendSocketMessage(senderClient,data, "get_message", getBoardIdParam(senderClient));
        };
    }

    private String getBoardIdParam(SocketIOClient senderClient) {
        return senderClient.getHandshakeData().getUrlParams().get("boardId").get(0);
    }

    private DataListener<UserCommentRequest> onCommentDeleted() {
        return (senderClient, data, ackSender) -> {
            String boardIdParam = getBoardIdParam(senderClient);
            UserCommentResponse userComment = commentService.findById(data.getId());
            commentService.deleteById(data.getId());
            data.setCommentType(userComment.getCommentType());
            data.setContent(userComment.getContent());
            data.setUserId(userComment.getUserId());
            data.setBoardId(boardIdParam);
            sendSocketMessage(senderClient,data, "deleted_message", boardIdParam);
        };
    }

    private DataListener<UserCommentRequest> onCommentUpdated() {
        return (senderClient, data, ackSender) -> {
            commentService.update(data);
            sendSocketMessage(senderClient,data, "updated_message", getBoardIdParam(senderClient));
        };
    }

    private ConnectListener onConnected() {
        return (client) -> {
            var params = client.getHandshakeData().getUrlParams();
            String jwtToken = client.getHandshakeData().getHttpHeaders().get("Authorization");
            String board = params.get("boardId").get(0);

            boolean isUserMemberOfBoard;

            try {
                isUserMemberOfBoard = boardService.isUserMemberOfBoard(board, jwtToken);
            }catch (Exception e) {
                log.info("User is not belong to this board! {}, {}", client.getSessionId().toString(), board);
                client.disconnect();
                return;
            }

            if(!isUserMemberOfBoard)
            {
                log.info("User is not belong to this board! {}, {}", client.getSessionId().toString(), board);
                client.disconnect();
                return;
            }

            client.joinRoom(board);
        };
    }

    private DisconnectListener onDisconnected() {
        return client -> {};
    }

}