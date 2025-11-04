package com.project1.project1.dto.mappers;

import com.project1.project1.dto.CommentCreateDto;
import com.project1.project1.dto.CommentDto;
import com.project1.project1.model.Comment;
import com.project1.project1.model.Post;
import com.project1.project1.model.User;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

@Mapper(componentModel = "spring",uses=UserMapper.class)

public interface CommentMapper {

    @Mapping(target="owner",source="owner", qualifiedByName = "mapOwnerIdToUser")
    @Mapping(target="post",source="post", qualifiedByName = "mapPostIdToPost")
    Comment toEntity(CommentCreateDto dto);

    @Mapping(target="owner",source="owner")
    @Mapping(target="postId",source="post")
    CommentDto toDto(Comment comment);


    @Named("mapOwnerIdToUser")
    default User mapOwnerIdToUser(int ownerId) {
        User user = new User();
        user.setId(ownerId);
        return user;
    }
     @Named("mapPostIdToPost")
    default Post mapPostIdToPost(int postId) {
        Post post = new Post();
        post.setId(postId);
        return post;
    }
}
