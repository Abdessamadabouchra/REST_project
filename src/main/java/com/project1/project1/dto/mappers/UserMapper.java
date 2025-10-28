package com.project1.project1.dto.mappers;


import com.project1.project1.dto.UserDto;
import com.project1.project1.model.User;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface UserMapper {

    User UserDtoToUser(UserDto userDto);

    UserDto UserToUserDto(User user);
}
