package com.project1.project1.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@Schema(description = "Data Transfer Object for a lightweight preview of a post")
public class PostPreviewDto {

    @Schema(description = "Unique identifier of the post", example = "b1ffbc99-9c0b-4ef8-bb6d-6bb9bd380a22")
    private UUID id;

    @Schema(description = "Text content of the post", example = "Short preview text...")
    private String text;

    @Schema(description = "Image URL", example = "https://example.com/thumb.png")
    private String image;

    @Schema(description = "Number of likes", example = "10")
    private int likes;

    @Schema(description = "Tags associated with the post", example = "[\"java\"]")
    private List<String> tags;

    @Schema(description = "Date the post was published", example = "2023-11-01")
    private LocalDate publishDate;

    @Schema(description = "Preview details of the post owner")
    private UserPreviewDto owner;

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

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

    public LocalDate getPublishDate() {
        return publishDate;
    }

    public void setPublishDate(LocalDate publishDate) {
        this.publishDate = publishDate;
    }

    public UserPreviewDto getOwner() {
        return owner;
    }

    public void setOwner(UserPreviewDto owner) {
        this.owner = owner;
    }
}