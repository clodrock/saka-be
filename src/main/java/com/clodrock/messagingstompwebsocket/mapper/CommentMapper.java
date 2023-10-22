package com.clodrock.messagingstompwebsocket.mapper;

import com.clodrock.messagingstompwebsocket.entity.Comment;
import com.clodrock.messagingstompwebsocket.model.UserCommentResponse;
import com.clodrock.messagingstompwebsocket.model.UserCommentSaveRequest;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface CommentMapper {
    UserCommentResponse toUserComment(Comment comment);

    Comment toComment(UserCommentSaveRequest comment);
}
