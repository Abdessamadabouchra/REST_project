package com.project1.project1.dto.mappers;

import com.project1.project1.dto.PostDto;
import com.project1.project1.dto.PostPreviewDto;
import com.project1.project1.dto.UserPreviewDto;
import com.project1.project1.model.Post;
import com.project1.project1.model.User;

import java.util.List;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface PostMapper {

    // Création : PostDto -> Post entity
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "publishDate", ignore = true)
    @Mapping(source = "ownerId", target = "owner.id")
    Post toEntity(PostPreviewDto dto);

    // Preview : Post entity -> PostDto
    @Mapping(source = "owner", target = "owner") // MapStruct va utiliser toUserPreviewDto
    PostPreviewDto toDto(Post post);

    // Full return : Post entity -> PostResponseDto
    PostDto toResponseDto(Post post);

    // Mapping liste
    List<PostPreviewDto> toDtoList(List<Post> posts);
    List<PostDto> toResponseDtoList(List<Post> posts);

    // Méthode pour mapper User -> UserPreviewDto
    UserPreviewDto toUserPreviewDto(User user);

    PostPreviewDto updatePostDto(PostPreviewDto postPreviewDto,@MappingTarget Post post);
}
