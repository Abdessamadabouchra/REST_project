package com.project1.project1.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.project1.project1.model.Post;

public interface PostDao extends JpaRepository<Post, Integer> {
    
}
