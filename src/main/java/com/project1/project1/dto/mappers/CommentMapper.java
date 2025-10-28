package com.project1.project1.dto.mappers;

import com.project1.project1.dto.CommentDto;
import com.project1.project1.model.Comment;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;

@Mapper(componentModel = "spring")

public interface CommentMapper {

    @Mapping(target="userId",source="user.id")
    @Mapping(target="postId",source="post.id")
    CommentDto commentToCommentDto(Comment comment);

    @Mapping(target="user.id",source="userId")
    @Mapping(target="post.id",source="postId")
    Comment commentDtoToComment(CommentDto commentDto);
}
