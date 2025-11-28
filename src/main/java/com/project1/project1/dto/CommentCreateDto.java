package com.project1.project1.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.util.UUID;

@Schema(description = "Data Transfer Object for creating a new comment")
public class CommentCreateDto {

    @Schema(description = "The content of the comment", example = "This is a great post! Thanks for sharing.", required = true, minLength = 6, maxLength = 1000)
    @Size(min = 6, max = 1000, message = "{comment.size}")
    @NotNull(message = "{comment.notnull.message}")
    private String message;

    @Schema(description = "The UUID of the user who is creating the comment", example = "a0eebc99-9c0b-4ef8-bb6d-6bb9bd380a11", required = true)
    @NotNull(message = "{comment.notnull.owner}")
    private UUID owner;

    @Schema(description = "The UUID of the post the comment belongs to", example = "b1ffbc99-9c0b-4ef8-bb6d-6bb9bd380a22", required = true)
    @NotNull(message = "{comment.notnull.post}")
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