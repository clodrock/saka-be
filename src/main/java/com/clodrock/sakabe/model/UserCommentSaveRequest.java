package com.clodrock.sakabe.model;

import com.clodrock.sakabe.enums.CommentType;
import lombok.Builder;

@Builder
public record UserCommentSaveRequest(String content, CommentType commentType, Long userId) {
}
