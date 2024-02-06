package com.clodrock.sakabe.mapper;

import com.clodrock.sakabe.entity.UserComment;
import com.clodrock.sakabe.enums.CommentType;
import com.clodrock.sakabe.model.SubCommentRequest;
import com.clodrock.sakabe.model.UserCommentResponse;
import com.clodrock.sakabe.model.UserCommentRequest;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;

import java.util.List;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING, imports = CommentType.class)
public interface CommentMapper {
    @Mapping(source = "subComments", target = "childList")
    UserCommentResponse toUserComment(UserComment comment, List<UserCommentResponse> subComments);

    UserCommentResponse toUserComment(UserComment comment);

    UserComment toComment(UserCommentRequest comment);

    @Mapping(target = "commentType", expression = "java(CommentType.SUB_COMMENT)")
    UserComment fromSubComment(SubCommentRequest subComment);
}
