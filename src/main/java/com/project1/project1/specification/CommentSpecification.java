package com.project1.project1.specification;

import com.project1.project1.model.Comment;
import jakarta.persistence.criteria.Predicate;
import org.springframework.data.jpa.domain.Specification;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class CommentSpecification {

    public static Specification<Comment> filter(String message, LocalDate startDate, LocalDate endDate){
        return (root, query, cb) -> {
            List<Predicate> predicates = new ArrayList<>();
            if (message != null) {
                predicates.add(cb.equal(root.get("message"), message));
            }
            if(startDate != null){
                predicates.add(cb.greaterThanOrEqualTo(root.get("publishDate"), startDate));
            }
            if(endDate != null){
                predicates.add(cb.lessThanOrEqualTo(root.get("publishDate"), endDate));
            }
            return cb.and(predicates.toArray(new Predicate[0]));
        };
    }
}
