package com.project1.project1.model;


import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.project1.project1.enums.Title;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.LocalDate;

@Entity
public class User {

    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    private int id;

    @Enumerated(EnumType.STRING)
    private Title title;
    @Size(min = 2,max = 50,message = "First name should be between 2 and 50 character")
    private String firstName;
    @Size(min = 2,max = 50,message = "Last name should be between 2 and 50 character")
    private String lastName;
    @Email(regexp = "[a-z0-9._%+-]+@[a-z0-9.-]+\\.[a-z]{2,3}",
            flags = Pattern.Flag.CASE_INSENSITIVE)
    private String email;
    private LocalDate dateOfBirth;
    private LocalDate registerDate;
    private String phone;
    private String picture;
    @Embedded
    private Location location;

    public User() {}

    public User(Title title, String firstName, String lastName, String email, LocalDate dateOfBirth,  String phone, String picture, Location location) {
        this.title = title;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.dateOfBirth = LocalDate.now();
        this.phone = phone;
        this.picture = picture;
        this.location = location;
    }
}
