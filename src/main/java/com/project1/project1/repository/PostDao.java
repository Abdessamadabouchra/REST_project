package com.project1.project1.repository;

import com.project1.project1.model.Post;        
import org.springframework.data.jpa.repository.JpaRepository;


public interface PostDao extends JpaRepository<Post, Integer> {

}
