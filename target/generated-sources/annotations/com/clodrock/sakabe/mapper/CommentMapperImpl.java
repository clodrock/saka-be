package com.clodrock.sakabe.mapper;

import com.clodrock.sakabe.entity.Comment;
import com.clodrock.sakabe.model.UserCommentResponse;
import com.clodrock.sakabe.model.UserCommentSaveRequest;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    comments = "version: 1.5.5.Final, compiler: javac, environment: Java 17.0.1 (Oracle Corporation)"
)
@Component
public class CommentMapperImpl implements CommentMapper {

    @Override
    public UserCommentResponse toUserComment(Comment comment) {
        if ( comment == null ) {
            return null;
        }

        UserCommentResponse.UserCommentResponseBuilder userCommentResponse = UserCommentResponse.builder();

        userCommentResponse.id( comment.getId() );
        userCommentResponse.content( comment.getContent() );
        userCommentResponse.commentType( comment.getCommentType() );
        userCommentResponse.userId( comment.getUserId() );

        return userCommentResponse.build();
    }

    @Override
    public Comment toComment(UserCommentSaveRequest comment) {
        if ( comment == null ) {
            return null;
        }

        Comment.CommentBuilder<?, ?> comment1 = Comment.builder();

        comment1.content( comment.content() );
        comment1.commentType( comment.commentType() );
        comment1.userId( comment.userId() );

        return comment1.build();
    }
}
