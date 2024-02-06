package com.clodrock.sakabe.service;

import com.clodrock.sakabe.entity.Board;
import com.clodrock.sakabe.entity.SakaUser;
import com.clodrock.sakabe.exception.AlreadyExistException;
import com.clodrock.sakabe.exception.InvalidAuthenticationException;
import com.clodrock.sakabe.exception.NotFoundException;
import com.clodrock.sakabe.mapper.BoardMapper;
import com.clodrock.sakabe.model.AddUserRequest;
import com.clodrock.sakabe.model.CreateBoardRequest;
import com.clodrock.sakabe.model.CreateBoardResponse;
import com.clodrock.sakabe.repository.BoardRepository;
import io.micrometer.common.util.StringUtils;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@AllArgsConstructor
@Slf4j
public class BoardService {

    private final BoardRepository boardRepository;
    private final AuthenticationService authenticationService;
    private final JwtService jwtService;
    private final UserService userService;
    private final BoardMapper boardMapper;


    public CreateBoardResponse createBoard(CreateBoardRequest request){
        if(boardRepository.findByName(request.getBoardName())
                .isPresent())
            throw new AlreadyExistException("Board already exist with board name " + request.getBoardName());

        String currentUser = authenticationService.getActiveUsername();
        request.getOwnerList().add(currentUser);
        request.getUserList().add(currentUser);

        request.getUserList().addAll(request.getOwnerList());

        List<SakaUser> userList = userService.findUsersByEmailList(request.getUserList());
        List<SakaUser> ownerList = userService.findUsersByEmailList(request.getOwnerList());

        Board board = Board.builder()
                .name(request.getBoardName())
                .ownerList(ownerList)
                .userList(userList)
                .boardCreator(currentUser)
                .build();

        Board createdBoard = boardRepository.save(board);
        return boardMapper.toResponse(createdBoard);
    }

    public CreateBoardResponse addUser(AddUserRequest request) {
        List<SakaUser> usersByEmailList = userService.findUsersByEmailList(request.getEmailList());
        Optional<Board> board = boardRepository.findById(request.getBoardId());

        return board.map(b-> {
            b.getUserList().addAll(usersByEmailList);
            boardRepository.save(b);
            return boardMapper.toResponse(b);
        }).orElseThrow(()-> new NotFoundException("Board could not be found!"));
    }

    public List<CreateBoardResponse> getUserSubscribedBoards(){
        List<Board> boards = boardRepository.findBoardsByUser(authenticationService.getActiveUsername());
        if(boards.isEmpty()) log.info("Current user is not member to any board!");
        return boards.stream().map(boardMapper::toResponse).toList();
    }

    public List<CreateBoardResponse> getUserOwnedBoards(){
        List<Board> boards = boardRepository.findBoardsByOwner(authenticationService.getActiveUsername());
        if(boards.isEmpty()) log.info("Current user have not any board yet, username" + authenticationService.getActiveUsername());
        return boards.stream().map(boardMapper::toResponse).toList();
    }

    public void deleteBoard(String boardId) {
        String activeUsername = authenticationService.getActiveUsername();

        List<Board> boards = boardRepository.findByBoardCreator(activeUsername);

        if(boards.isEmpty() || boards.stream().noneMatch(p-> p.getId().equals(boardId)))
            throw new NotFoundException("Board not found or user have not permission to delete this board!");
    }

    public boolean isUserMemberOfBoard(String board, String jwtToken) {
        checkToken(jwtToken);
        String trimmedToken = jwtToken.substring(7);
        String username = jwtService.extractUsername(trimmedToken);

        checkToken(username);

        List<Board> boards = boardRepository.findBoardsByUser(username);

        if(boards.isEmpty())
            throw new InvalidAuthenticationException("Current user is not member to any board!");

        return boards.stream().anyMatch(p -> p.getId().equals(UUID.fromString(board)));
    }

    private void checkToken(String token) {
        if(StringUtils.isEmpty(token))
            throw new InvalidAuthenticationException("User is not valid!");
    }
}
