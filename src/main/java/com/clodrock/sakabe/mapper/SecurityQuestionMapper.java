package com.clodrock.sakabe.mapper;

import com.clodrock.sakabe.entity.SakaProperty;
import com.clodrock.sakabe.model.SecurityQuestion;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface SecurityQuestionMapper {

    @Mapping(target = "id", expression = "java(Integer.valueOf(sakaProperty.getValue()))")
    @Mapping(target = "question", source = "key")
    SecurityQuestion toSecurityQuestion(SakaProperty sakaProperty);
}
