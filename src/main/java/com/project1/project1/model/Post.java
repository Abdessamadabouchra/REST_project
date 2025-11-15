package com.project1.project1.model;

import jakarta.persistence.*;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@Entity
public class Post {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;
    private String text;
    private String image;
    private int likes = 0;
    private String link;

    @ElementCollection
    private List<String> tags;

    private LocalDate publishDate=LocalDate.now();

    @ManyToOne
    @JoinColumn(name = "owner_id")
    private User owner;

    public Post() {}

    public Post(String text, String image, int likes, String link, List<String> tags, User owner) {
        this.text = text;
        this.image = image;
        this.likes = likes;
        this.link = link;
        this.tags = tags;
        this.owner = owner;
    }

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

    public User getOwner() {
        return owner;
    }

    public void setOwner(User owner) {
        this.owner = owner;
    }
}