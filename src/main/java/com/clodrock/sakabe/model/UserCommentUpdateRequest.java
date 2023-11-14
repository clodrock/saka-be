package com.clodrock.sakabe.model;

import com.clodrock.sakabe.enums.CommentType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class UserCommentUpdateRequest{
    private Long id;
    private String content;
    private CommentType commentType;
}
