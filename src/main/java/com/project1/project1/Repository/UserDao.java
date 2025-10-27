package com.project1.project1.Repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.project1.project1.model.User;

public interface UserDao extends JpaRepository<User, Integer> {
    
}

    
