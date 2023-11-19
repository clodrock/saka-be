package com.clodrock.sakabe.service;

import com.clodrock.sakabe.entity.Board;
import com.clodrock.sakabe.entity.SakaUser;
import com.clodrock.sakabe.exception.NotFoundException;
import com.clodrock.sakabe.mapper.BoardMapper;
import com.clodrock.sakabe.model.AddUserRequest;
import com.clodrock.sakabe.model.CreateBoardResponse;
import com.clodrock.sakabe.repository.BoardRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@AllArgsConstructor
public class BoardService {

    private final BoardRepository boardRepository;
    private final AuthenticationService authenticationService;
    private final UserService userService;
    private final BoardMapper boardMapper;


    public CreateBoardResponse createBoard(CreateBoardRequest request){
        String currentUser = authenticationService.getActiveUsername();
        request.getOwnerList().add(currentUser);
        request.getUserList().add(currentUser);

        List<SakaUser> userList = userService.findUsersByEmailList(request.getUserList());
        List<SakaUser> ownerList = userService.findUsersByEmailList(request.getOwnerList());

        Board board = Board.builder()
                .boardId(UUID.randomUUID().toString())
                .name(request.getBoardName())
                .ownerList(ownerList)
                .userList(userList).build();

        Board createdBoard = boardRepository.save(board);
        return boardMapper.toResponse(createdBoard);
    }

    public CreateBoardResponse addUser(AddUserRequest request) {
        List<SakaUser> usersByEmailList = userService.findUsersByEmailList(request.getEmailList());
        Optional<Board> board = boardRepository.findByBoardId(request.getBoardId());

        return board.map(b-> {
            b.getUserList().addAll(usersByEmailList);
            boardRepository.save(b);
            return boardMapper.toResponse(b);
        }).orElseThrow(()-> new NotFoundException("Board could not be found!"));
    }

    public List<CreateBoardResponse> getUserInvolvedBoards(){
        List<Board> boards = boardRepository.findBoardsByUser(authenticationService.getActiveUsername());
        if(boards.isEmpty()) throw new NotFoundException("Current user is not member to any board!");
        return boards.stream().map(boardMapper::toResponse).toList();
    }

    public List<CreateBoardResponse> getUserOwnedBoards(){
        List<Board> boards = boardRepository.findBoardsByOwner(authenticationService.getActiveUsername());
        if(boards.isEmpty()) throw new NotFoundException("Current user have not any board yet!");
        return boards.stream().map(boardMapper::toResponse).toList();
    }
}
