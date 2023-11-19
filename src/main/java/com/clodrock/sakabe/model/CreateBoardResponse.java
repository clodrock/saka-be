package com.clodrock.sakabe.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CreateBoardResponse {
    private String boardId;
    private String boardName;
    private List<String> boardUsers;
    private List<String> boardOwners;
}
