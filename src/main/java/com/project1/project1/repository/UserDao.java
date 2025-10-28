package com.project1.project1.repository;

import com.project1.project1.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserDao  extends JpaRepository<User, Integer> {
    
}
