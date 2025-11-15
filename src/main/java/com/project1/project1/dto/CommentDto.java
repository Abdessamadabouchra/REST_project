package com.project1.project1.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.time.LocalDate;
import java.util.UUID;

public class CommentDto {

    private UUID id;
    @NotNull(message = "User ID is mandatory")
    private UUID userId;

    @NotNull(message = "Post ID is mandatory")
    private UUID postId;

    @Size(min = 2, max = 500, message = "the message should be between 2 and 500 charachter")
    @NotNull(message = "The message field cannot be null")
    private String message;
    private LocalDate publishedDate;


    public void setId(UUID id) {
        this.id = id;
    }

    public UUID getId() {
        return id;
    }

    public UUID getUserId() {
        return userId;
    }

    public void setUserId(UUID userId) {
        this.userId = userId;
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

    public void setPublishedDate() {
        this.publishedDate = LocalDate.now();
    }
    public LocalDate getPublishedDate() {
        return publishedDate;
    }

}