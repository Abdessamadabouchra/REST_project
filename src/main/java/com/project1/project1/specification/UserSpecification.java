package com.project1.project1.specification;


import com.project1.project1.model.User;
import org.springframework.data.jpa.domain.Specification;

import java.time.LocalDate;

public class UserSpecification {
    public static Specification<User> filter(LocalDate startDateOfBirth,LocalDate endDateOfBirth,LocalDate startRegisterDate,LocalDate endRegisterDate,String country,String state,String city,String timezone){

    }
}
