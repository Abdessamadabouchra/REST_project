package com.project1.project1.dto.mappers;

import com.project1.project1.dto.PostDto;
import com.project1.project1.model.Post;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;


@Mapper(componentModel = "spring")
public interface PostMapper {

    @Mapping(source = "owner.id", target = "ownerId")
    @Mapping(source = "owner.title", target = "ownerTitle")
    @Mapping(source = "owner.firstName", target = "ownerFirstName")
    @Mapping(source = "owner.lastName", target = "ownerLastName")
    @Mapping(source = "owner.picture", target = "ownerPicture")
    PostDto toDto(Post post);

    @Mapping(source = "ownerId", target = "owner", ignore = true)
    Post toEntity(PostDto dto);
}