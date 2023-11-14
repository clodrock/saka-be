package com.clodrock.sakabe.model;

import com.clodrock.sakabe.enums.CommentType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Data
public class UserCommentSaveRequest{
    private String content;
    private CommentType commentType;
    private Long userId;
    private Long id;
}
