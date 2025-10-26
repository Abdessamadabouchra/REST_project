package com.project1.project1.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Size;

@Embeddable
public class Location {
    @Size(min=5, max=100,message = "Steet should be between 5 and 100 charatcter")
    private String street;
    @Size(min=2, max=30,message = "City should be between 5 and 30 charatcter")
    private String city;
    @Size(min=2, max=30,message = "Steet should be between 5 and 30 charatcter")
    private String state;
    @Size(min=2, max=30,message = "Steet should be between 5 and 30 charatcter")
    private String country;
    private String timezone;

}
