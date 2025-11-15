package com.project1.project1.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import org.hibernate.validator.constraints.URL;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

public class PostDto {

    @Size(min = 6, max = 1000)
    @NotNull
    private String text;

    @URL(message = "Image must be a valid URL")
    private String image;

    private int likes;

    @Size(min = 6, max = 600)
    private String link;

    private List<String> tags;

    private LocalDate publishDate;

    @NotNull
    private UUID ownerId;
    private String ownerTitle;
    private String ownerFirstName;
    private String ownerLastName;
    private String ownerPicture;


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

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
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

    public UUID getOwnerId() {
        return ownerId;
    }

    public void setOwnerId(UUID ownerId) {
        this.ownerId = ownerId;
    }
    public String getOwnerTitle() {
        return ownerTitle;
    }
    public void setOwnerTitle(String ownerTitle) {
        this.ownerTitle = ownerTitle;
    }
    public String getOwnerFirstName() {
        return ownerFirstName;
    }
    public void setOwnerFirstName(String ownerFirstName) {
        this.ownerFirstName = ownerFirstName;
    }
    public String getOwnerLastName() {
        return ownerLastName;
    }
    public void setOwnerLastName(String ownerLastName) {
        this.ownerLastName = ownerLastName;
    }
    public String getOwnerPicture() {
        return ownerPicture;
    }
    public void setOwnerPicture(String ownerPicture) {
        this.ownerPicture = ownerPicture;
    }

}
