package com.project1.project1.model;


import jakarta.persistence.*;
import jakarta.validation.constraints.Size;
import org.hibernate.validator.constraints.URL;

import java.time.LocalDate;
import java.util.List;

@Entity
public class Post {

    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    private int id;
    @Size(min=6,max=1000)
    private String text;
    @URL(message = "Image must be a valid URL")
    private String image;
    private int likes=0;
    @Size(min=6,max=600)
    private String link;


    @ElementCollection
    private List<String> tags;

    private LocalDate publishDate;

    @ManyToOne
    @JoinColumn(name = "owner_id")
    private User owner;

    public Post() {}

    public Post(String text, String image, int likes, String link, List<String> tags, LocalDate publishDate, User owner) {
        this.text = text;
        this.image = image;
        this.likes = likes;
        this.link = link;
        this.tags = tags;
        this.publishDate = publishDate;
        this.owner = owner;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
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
