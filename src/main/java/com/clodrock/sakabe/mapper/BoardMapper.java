package com.clodrock.sakabe.mapper;

import com.clodrock.sakabe.entity.Board;
import com.clodrock.sakabe.entity.SakaUser;
import com.clodrock.sakabe.model.CreateBoardResponse;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface BoardMapper {
    default CreateBoardResponse toResponse(Board board){
        return CreateBoardResponse.builder()
                .boardId(board.getBoardId())
                .boardName(board.getName())
                .boardUsers(board.getUserList().stream().map(SakaUser::getEmail).toList())
                .boardOwners(board.getOwnerList().stream().map(SakaUser::getEmail).toList())
                .build();
    }
}
