package com.project1.project1.dto;

import com.project1.project1.enums.Title;
import com.project1.project1.model.Location;

import jakarta.validation.constraints.Size;
import jakarta.validation.constraints.NotNull;
import org.hibernate.validator.constraints.URL;

import java.util.UUID;


public class UserDto {

    private UUID id;
    private Title title;

    @Size(min = 2,max = 50,message = "First name should be between 2 and 50 character")
    @NotNull
    private String firstName;

    @Size(min = 2,max = 50,message = "Last name should be between 2 and 50 character")
    @NotNull
    private String lastName;


    @URL(message = "Picture must be a valid URL")
    private String picture;

    private Location location;

    public UUID getId() {
        return id;
    }
    public void setId(UUID id) {
        this.id = id;
    }

    public Title getTitle() {
        return title;
    }

    public void setTitle(Title title) {
        this.title = title;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }


    public String getPicture() {
        return picture;
    }

    public void setPicture(String picture) {
        this.picture = picture;
    }

    public Location getLocation() {
        return location;
    }

    public void setLocation(Location location) {
        this.location = location;
    }
}