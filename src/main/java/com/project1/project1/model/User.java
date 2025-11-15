package com.project1.project1.model;

import com.project1.project1.enums.Title;
import jakarta.persistence.*;
import java.time.LocalDate;
import java.util.UUID;

@Entity
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Enumerated(EnumType.STRING)
    private Title title;
    private String firstName;
    private String lastName;
    private String email;
    private LocalDate dateOfBirth;
    //@Column(nullable = false, columnDefinition = "DATE DEFAULT CURRENT_DATE")
    private LocalDate registerDate = LocalDate.now();
    private String phone;
    private String picture;
    @Embedded
    private Location location;


    public User() {}

    public User(Title title, String firstName, String lastName, String email, LocalDate dateOfBirth , String phone, String picture, Location location) {
        this.title = title;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.dateOfBirth = dateOfBirth;
        this.phone = phone;
        this.picture = picture;
        this.registerDate=LocalDate.now();
        this.location = location;
    }

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

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public LocalDate getDateOfBirth() {
        return dateOfBirth;
    }

    public void setDateOfBirth(LocalDate dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }

    public LocalDate getRegisterDate() {
        return registerDate;
    }

    public void setRegisterDate(LocalDate registerDate) {
        this.registerDate = LocalDate.now();
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
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