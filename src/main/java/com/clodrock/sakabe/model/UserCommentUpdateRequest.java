package com.clodrock.sakabe.model;

import com.clodrock.sakabe.enums.CommentType;
import lombok.Builder;

@Builder
public record UserCommentUpdateRequest(Long id, String content, CommentType commentType) {
}
