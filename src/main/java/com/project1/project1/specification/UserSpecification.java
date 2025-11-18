package com.project1.project1.specification;


import com.project1.project1.model.User;
import jakarta.persistence.criteria.Predicate;
import org.springframework.data.jpa.domain.Specification;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;


public class UserSpecification {
    public static Specification<User> filter(LocalDate startDateOfBirth,LocalDate endDateOfBirth,LocalDate startRegisterDate,LocalDate endRegisterDate,String country,String state,String city,String timezone){
        return (root, query, cb) -> {
            List<Predicate> predicates = new ArrayList<>();
            if (country != null) {
                predicates.add(cb.equal(root.get("location").get("country"), country));
            }
            if (state != null) {
                predicates.add(cb.equal(root.get("location").get("state"), state));
            }
            if (city != null) {
                predicates.add(cb.equal(root.get("location").get("city"),city));
            }
            if(timezone!=null){
                predicates.add(cb.equal(root.get("location").get("timezone"),timezone));
            }

            if(startDateOfBirth!=null){
                predicates.add(cb.greaterThanOrEqualTo(root.get("dateOfBirth"), startDateOfBirth));
            }
            if(endDateOfBirth!=null){
                predicates.add(cb.lessThanOrEqualTo(root.get("dateOfBirth"), endDateOfBirth));
            }
            if(startRegisterDate!=null){
                predicates.add(cb.greaterThanOrEqualTo(root.get("registerDate"), startRegisterDate));
            }
            if(endRegisterDate!=null){
                predicates.add(cb.lessThanOrEqualTo(root.get("registerDate"), endRegisterDate));
            }

            return cb.and(predicates.toArray(new Predicate[0]));
        };
    }
}
