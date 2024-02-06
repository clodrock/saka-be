package com.clodrock.sakabe.model;

import com.clodrock.sakabe.enums.CommentType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.UUID;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserCommentResponse{
    private Long id;
    private String content;
    private CommentType commentType;
    private UUID userId;
    private List<UserCommentResponse> childList;
}
