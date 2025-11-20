package com.project1.project1.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.time.LocalDate;
import java.util.UUID;

public class CommentDto {

    private UUID id;
    @NotNull(message = "User ID is mandatory")
    private UserPreviewDto user;

    @NotNull(message = "Post ID is mandatory")
    private UUID postId;

    @Size(min = 2, max = 500, message = "the message should be between 2 and 500 charachter")
    @NotNull(message = "The message field cannot be null")
    private String message;
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