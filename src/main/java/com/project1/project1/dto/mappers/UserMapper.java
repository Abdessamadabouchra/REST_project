package com.project1.project1.dto.mappers;


import com.project1.project1.dto.UserFullDto;
import com.project1.project1.dto.UserPreviewDto;
import com.project1.project1.model.User;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface UserMapper {

    UserPreviewDto toPreviewDto(User user);
    UserFullDto toFullDto(User user);
    User toEntity(UserFullDto dto);
}
