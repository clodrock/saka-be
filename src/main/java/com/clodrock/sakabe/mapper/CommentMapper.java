package com.clodrock.sakabe.mapper;

import com.clodrock.sakabe.entity.Comment;
import com.clodrock.sakabe.model.UserCommentResponse;
import com.clodrock.sakabe.model.UserCommentRequest;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface CommentMapper {
    UserCommentResponse toUserComment(Comment comment);

    Comment toComment(UserCommentRequest comment);
}
