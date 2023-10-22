package com.clodrock.messagingstompwebsocket.model;

import com.clodrock.messagingstompwebsocket.enums.CommentType;
import lombok.Builder;

@Builder
public record UserCommentUpdateRequest(Long id, String content, CommentType commentType) {
}
