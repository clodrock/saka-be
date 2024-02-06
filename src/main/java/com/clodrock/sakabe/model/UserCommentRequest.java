package com.clodrock.sakabe.model;

import com.clodrock.sakabe.enums.CommentType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Data
public class UserCommentRequest {
    private String content;
    private CommentType commentType;
    private UUID userId;
    private Long id;
    private UUID boardId;
    private Long parentId;
}
