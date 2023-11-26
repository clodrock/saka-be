package com.clodrock.sakabe.controller;

import com.clodrock.sakabe.model.AddUserRequest;
import com.clodrock.sakabe.model.CreateBoardResponse;
import com.clodrock.sakabe.model.SuccessResponse;
import com.clodrock.sakabe.service.BoardService;
import com.clodrock.sakabe.service.CreateBoardRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/board")
@RequiredArgsConstructor
public class BoardController {

    private final BoardService boardService;

    @PostMapping("/create")
    public ResponseEntity<CreateBoardResponse> create(@RequestBody @Valid CreateBoardRequest boardRequest){
        return ResponseEntity.ok(boardService.createBoard(boardRequest));
    }

    @PatchMapping("/add-user")
    public ResponseEntity<CreateBoardResponse> addUser(@RequestBody AddUserRequest addUserRequest){
        return ResponseEntity.ok(boardService.addUser(addUserRequest));
    }

    @GetMapping("/subscribed")
    public ResponseEntity<List<CreateBoardResponse>> getSubscribedBoards(){
        return ResponseEntity.ok(boardService.getUserSubscribedBoards());
    }

    @GetMapping("/user-own-boards")
    public ResponseEntity<List<CreateBoardResponse>> getUserOwnBoards(){
        return ResponseEntity.ok(boardService.getUserOwnedBoards());
    }

    @DeleteMapping("/delete/{boardId}")
    public ResponseEntity<SuccessResponse> deleteBoard(@PathVariable("boardId") String boardId) {
        boardService.deleteBoard(boardId);
        return ResponseEntity.ok().body(SuccessResponse.builder().build());
    }
}
