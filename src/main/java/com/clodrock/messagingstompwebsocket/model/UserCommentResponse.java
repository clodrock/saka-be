package com.clodrock.messagingstompwebsocket.model;

import com.clodrock.messagingstompwebsocket.enums.CommentType;
import lombok.Builder;

@Builder
public record UserCommentResponse(Long id, String content, CommentType commentType, Long userId){
}
