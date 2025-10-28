package com.project1.project1.dto.mappers;

import com.project1.project1.dto.CommentDto;
import com.project1.project1.model.Comment;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")

public interface CommentMapper {

    CommentDto commentToCommentDto(Comment comment);

    Comment commentDtoToComment(CommentDto commentDto);
}
