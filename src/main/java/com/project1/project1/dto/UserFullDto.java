package com.project1.project1.dto;

import com.project1.project1.enums.Title;
import com.project1.project1.model.Location;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Size;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;

import org.hibernate.validator.constraints.URL;

import java.util.UUID;

@Schema(description = "Data Transfer Object representing full user profile details")
public class UserFullDto {

    @Schema(description = "Unique identifier of the user", example = "a0eebc99-9c0b-4ef8-bb6d-6bb9bd380a11")
    private UUID id;

    @Schema(description = "User's title", example = "MR", required = true)
    @NotNull(message = "{user.notnull.title}")
    private Title title;

    @Schema(description = "First name of the user", example = "John", required = true, minLength = 2, maxLength = 50)
    @NotNull(message = "{user.notnull.firstname}")
    @Size(min = 2, max = 50, message = "{user.firstname.size")
    private String firstName;

    @Schema(description = "Last name of the user", example = "Doe", required = true, minLength = 2, maxLength = 50)
    @NotNull(message = "{user.notnull.lastname}")
    @Size(min = 2, max = 50, message = "{user.lastname.size")
    private String lastName;

    @Schema(description = "Email address (must be valid format)", example = "john.doe@example.com", required = true)
    @NotNull(message = "{user.notnull.email}")
    @Email(message = "{user.notvalid.email}")
    private String email;

    @Schema(description = "Date of birth (ISO: YYYY-MM-DD)", example = "1990-01-01")
    private LocalDate dateOfBirth;

    @Schema(description = "Date the user registered (ISO: YYYY-MM-DD)", example = "2023-01-01")
    private LocalDate registerDate;

    @Schema(description = "Phone number", example = "+1234567890")
    private String phone;

    @Schema(description = "URL to profile picture", example = "https://example.com/avatar.jpg")
    @URL(message = "{error.image.url}")
    private String picture;

    @Schema(description = "User's address location")
    @Valid
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
        this.registerDate = registerDate;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }
}