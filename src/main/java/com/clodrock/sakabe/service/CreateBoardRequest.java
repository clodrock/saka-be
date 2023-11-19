package com.clodrock.sakabe.service;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CreateBoardRequest {
    private String boardName;
    private List<String> userList;
    private List<String> ownerList;
}
