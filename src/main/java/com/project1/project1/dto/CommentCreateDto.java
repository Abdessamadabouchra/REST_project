package com.project1.project1.dto;

public class CommentCreateDto {
    private String message;
    private int owner;
    private int post;

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public int getOwner() {
        return owner;
    }
    public void setOwner(int owner) {
        this.owner = owner;
    }
    public int getPost() {
        return post;
    }
    public void setPost(int post) {
        this.post = post;
    }

    
}
