package com.project1.project1.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;


@Embeddable
public class Location {
    @Size(min=5, max=100,message = "{location.street.size}")
    private String street;
    @Size(min=2, max=30,message = "{location.city.size}")
    private String city;
    @Size(min=2, max=30,message ="{location.state.size}")
    private String state;
    @Size(min=2, max=30,message = "{location.country.size}")
    private String country;
    @Pattern(regexp = "^[+-](?:2[0-3]|[01][0-9]):[0-5][0-9]$", message = "{location.timezone.reg}")
    private String timezone;


    public Location() {}

    public  Location(String street, String city, String state, String country, String timezone) {
        this.street = street;
        this.city = city;
        this.state = state;
        this.country = country;
        this.timezone = timezone;
    }


    public String getStreet() { return street; }
    public void setStreet(String street) { this.street = street; }

    public String getCity() { return city; }
    public void setCity(String city) { this.city = city; }

    public String getState() { return state; }
    public void setState(String state) { this.state = state; }

    public String getCountry() { return country; }
    public void setCountry(String country) { this.country = country; }

    public String getTimezone() { return timezone; }
    public void setTimezone(String timezone) { this.timezone = timezone; }

}
