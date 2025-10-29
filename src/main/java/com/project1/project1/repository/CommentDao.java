package com.project1.project1.repository;

import com.project1.project1.model.Comment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CommentDao extends JpaRepository<Comment,Integer> {
    Page<Comment> findAll(Pageable pageable);
}
