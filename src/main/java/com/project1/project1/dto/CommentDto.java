package com.project1.project1.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.time.LocalDate;
import java.util.UUID;

@Schema(description = "Data Transfer Object representing a comment")
public class CommentDto {

    @Schema(description = "Unique identifier of the comment", example = "c2eebc99-9c0b-4ef8-bb6d-6bb9bd380a33")
    private UUID id;

    @Schema(description = "The user who created the comment")
    private UserPreviewDto user;

    @Schema(description = "The ID of the post this comment is associated with", example = "b1ffbc99-9c0b-4ef8-bb6d-6bb9bd380a22")
    private UUID postId;

    @Schema(description = "Content of the comment", example = "This is a great post!")
    private String message;

    @Schema(description = "Date when the comment was published", example = "2023-10-25")
    private LocalDate publishDate;

    public void setId(UUID id) {
        this.id = id;
    }

    public UUID getId() {
        return id;
    }

    public UserPreviewDto getUser() {
        return user;
    }

    public void setUser(UserPreviewDto user) {
        this.user = user;
    }

    public UUID getPostId() {
        return postId;
    }

    public void setPostId(UUID postId) {
        this.postId = postId;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public void setPublishDate(LocalDate publishDate) {
        this.publishDate = publishDate;
    }

    public LocalDate getPublishDate() {
        return publishDate;
    }
}