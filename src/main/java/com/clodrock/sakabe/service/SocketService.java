package com.clodrock.sakabe.service;

import com.clodrock.sakabe.model.EmojiRequest;
import com.clodrock.sakabe.model.SubCommentRequest;
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
import java.util.Optional;
import java.util.UUID;

@Component
@Slf4j
public class SocketService {
    private final SocketIOServer socketIoServer;
    private final CommentService commentService;
    private final BoardService boardService;
    private final CommentInteractionService commentInteractionService;


    public SocketService(SocketIOServer socketIoServer, CommentService commentService, BoardService boardService, CommentInteractionService commentInteractionService) {
        this.socketIoServer = socketIoServer;
        this.commentService = commentService;
        this.boardService = boardService;
        this.commentInteractionService = commentInteractionService;
        socketIoServer.addConnectListener(onConnected());
        socketIoServer.addDisconnectListener(onDisconnected());
        socketIoServer.addEventListener("send_message", UserCommentRequest.class, onCommentReceived());
        socketIoServer.addEventListener("delete_message", UserCommentRequest.class, onCommentDeleted());
        socketIoServer.addEventListener("update_message", UserCommentRequest.class, onCommentUpdated());
        socketIoServer.addEventListener("emoji", EmojiRequest.class, onEmojiReceived());
        socketIoServer.addEventListener("send_sub_comment", SubCommentRequest.class, subCommentReceived());
    }

    public void sendSocketMessage(SocketIOClient senderClient, UserCommentRequest userCommentRequest, String streamType, String boardId) {
        Collection<SocketIOClient> clients = senderClient.getNamespace().getRoomOperations(boardId).getClients();

        for (SocketIOClient client : clients) {
                client.sendEvent(streamType, userCommentRequest);
        }
    }

    public void sendSocketMessage(SocketIOClient senderClient, EmojiRequest userCommentRequest, String streamType, String boardId) {
        Collection<SocketIOClient> clients = senderClient.getNamespace().getRoomOperations(boardId).getClients();

        for (SocketIOClient client : clients) {
            client.sendEvent(streamType, userCommentRequest);
        }
    }

    public void sendSocketMessage(SocketIOClient senderClient, SubCommentRequest subCommentRequest, String streamType, String boardId) {
        Collection<SocketIOClient> clients = senderClient.getNamespace().getRoomOperations(boardId).getClients();

        for (SocketIOClient client : clients) {
            client.sendEvent(streamType, subCommentRequest);
        }
    }

    private DataListener<EmojiRequest> onEmojiReceived() {
        return (senderClient, data, ackSender) -> {
            String boardIdParam = getBoardIdParam(senderClient);
            commentInteractionService.save(data, UUID.fromString(boardIdParam));
            sendSocketMessage(senderClient,data, "emoji_received", boardIdParam);
        };
    }

    private DataListener<SubCommentRequest> subCommentReceived() {
        return (senderClient, data, ackSender) -> {
            String boardIdParam = getBoardIdParam(senderClient);
            data.setBoardId(UUID.fromString(boardIdParam));
            commentService.saveSubComment(data);
            sendSocketMessage(senderClient,data, "sub_comment_received", boardIdParam);
        };
    }

    private DataListener<UserCommentRequest> onCommentReceived() {
        return (senderClient, data, ackSender) -> {
            String boardIdParam = getBoardIdParam(senderClient);
            data.setBoardId(UUID.fromString(boardIdParam));
            var userCommentResponse = commentService.saveComment(data);
            data.setId(userCommentResponse.getId());
            sendSocketMessage(senderClient,data, "get_message", boardIdParam);
        };
    }

    private String getBoardIdParam(SocketIOClient senderClient) {
        return senderClient.getHandshakeData().getSingleUrlParam("boardId");
    }

    private DataListener<UserCommentRequest> onCommentDeleted() {
        return (senderClient, data, ackSender) -> {
            String boardIdParam = getBoardIdParam(senderClient);
            Optional<UserCommentResponse> userComment = commentService.findById(data.getId());
            if(userComment.isPresent()) {
                commentService.deleteById(data.getId());
                data.setCommentType(userComment.get().getCommentType());
                data.setContent(userComment.get().getContent());
                data.setUserId(userComment.get().getUserId());
                data.setBoardId(UUID.fromString(boardIdParam));
                sendSocketMessage(senderClient, data, "deleted_message", boardIdParam);
            }
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
            String jwtToken = client.getHandshakeData().getSingleUrlParam("Authorization");
            String board = client.getHandshakeData().getSingleUrlParam("boardId");

            boolean isUserMemberOfBoard;

            try {
                isUserMemberOfBoard = boardService.isUserMemberOfBoard(board, jwtToken);
            }catch (Exception e) {
                log.info("Username : boardId {},{}", jwtToken, board);
                log.info("User is not belong to this board! {}, {}", client.getSessionId().toString(), board);
                client.disconnect();
                return;
            }

            if(!isUserMemberOfBoard)
            {
                log.info("Username : boardId {},{}", jwtToken, board);
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