package com.project1.project1.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.time.LocalDate;

public class CommentDto {

    private int id;
    @NotNull(message = "User ID is mandatory")
    private Integer userId;

    @NotNull(message = "Post ID is mandatory")
    private Integer postId;

    @Size(min = 2, max = 500, message = "the message should be between 2 and 500 charachter")
    @NotNull(message = "The message field cannot be null")
    private String message;
    private LocalDate publishedDate;


    public void setId(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public Integer getPostId() {
        return postId;
    }

    public void setPostId(Integer postId) {
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