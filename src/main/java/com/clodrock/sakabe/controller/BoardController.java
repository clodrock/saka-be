package com.clodrock.sakabe.controller;

import com.clodrock.sakabe.model.AddUserRequest;
import com.clodrock.sakabe.model.CreateBoardResponse;
import com.clodrock.sakabe.service.BoardService;
import com.clodrock.sakabe.service.CreateBoardRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/board")
@RequiredArgsConstructor
public class BoardController {

    private final BoardService boardService;

    @PostMapping("/create-board")
    public ResponseEntity<CreateBoardResponse> create(@RequestBody CreateBoardRequest boardRequest){
        return ResponseEntity.ok(boardService.createBoard(boardRequest));
    }

    @PostMapping("/add-user")
    public ResponseEntity<CreateBoardResponse> addUser(@RequestBody AddUserRequest addUserRequest){
        return ResponseEntity.ok(boardService.addUser(addUserRequest));
    }

    @GetMapping("/user-inv-boards")
    public ResponseEntity<List<CreateBoardResponse>> getUserInvolvedBoards(){
        return ResponseEntity.ok(boardService.getUserInvolvedBoards());
    }

    @GetMapping("/user-own-boards")
    public ResponseEntity<List<CreateBoardResponse>> getUserOwnBoards(){
        return ResponseEntity.ok(boardService.getUserOwnedBoards());
    }
}
