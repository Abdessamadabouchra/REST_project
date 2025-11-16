package com.project1.project1.dto;

import java.util.UUID;

public class CommentCreateDto {
    private String message;
    private UUID owner;
    private UUID post;

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public UUID getOwner() {
        return owner;
    }
    public void setOwner(UUID owner) {
        this.owner = owner;
    }
    public UUID getPost() {
        return post;
    }
    public void setPost(UUID post) {
        this.post = post;
    }

    
}
