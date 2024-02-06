package com.clodrock.sakabe.model;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
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
    private @NotBlank(message = "boardName cannot be empty") String boardName;
    private @NotEmpty(message = "userList cannot be empty!") List<String> userList;
    private @NotEmpty(message = "ownerList cannot be empty!") List<String> ownerList;
}
