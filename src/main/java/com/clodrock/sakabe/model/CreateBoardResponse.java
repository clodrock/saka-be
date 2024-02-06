package com.clodrock.sakabe.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CreateBoardResponse {
    private UUID boardId;
    private String boardName;
    private List<String> boardUsers;
    private List<String> boardOwners;
}
