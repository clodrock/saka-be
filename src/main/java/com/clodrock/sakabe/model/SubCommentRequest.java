package com.clodrock.sakabe.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Data
public class SubCommentRequest{
        private String content;
        private UUID userId;
        private Long parentId;
        private UUID boardId;
}
