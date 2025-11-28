package com.project1.project1.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import org.hibernate.validator.constraints.URL;
import java.util.List;
import java.util.UUID;

@Schema(description = "Data Transfer Object for creating a new post")
public class PostCreateDto {

    @Schema(description = "The main text content of the post", example = "Learning Spring Boot and Swagger is fun!", required = true, minLength = 6, maxLength = 1000)
    @Size(min = 6, max = 1000, message = "{post.text.size}")
    @NotNull(message = "{post.notnull.text}")
    private String text;

    @Schema(description = "URL to an image associated with the post", example = "https://example.com/image.jpg")
    @URL(message = "{error.image.url}")
    private String image;

    @Schema(description = "Initial number of likes (usually 0 for new posts)", example = "0")
    private int likes;

    @Schema(description = "List of tags associated with the post", example = "[\"spring\", \"java\", \"api\"]")
    private List<String> tags;

    @Schema(description = "UUID of the user creating the post", example = "a0eebc99-9c0b-4ef8-bb6d-6bb9bd380a11", required = true)
    @NotNull(message = "{post.notnull.owner}")
    private UUID owner;

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public int getLikes() {
        return likes;
    }

    public void setLikes(int likes) {
        this.likes = likes;
    }

    public List<String> getTags() {
        return tags;
    }

    public void setTags(List<String> tags) {
        this.tags = tags;
    }

    public UUID getOwner() {
        return owner;
    }

    public void setOwner(UUID owner) {
        this.owner = owner;
    }
}