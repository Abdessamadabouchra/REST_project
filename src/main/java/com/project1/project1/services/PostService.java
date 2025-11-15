package com.project1.project1.services;
import com.project1.project1.model.User;
import com.project1.project1.repository.UserDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class PostService {
    @Autowired
    private UserDao userDao;

    public List<User> getAllUsers() {
        return userDao.findAll();
    }

    public Optional<User> getUserById(UUID id) {
        return userDao.findById(id);
    }

    public User createUser(User user) {
        return userDao.save(user);
    }

    public void deleteUser(UUID id) {
        userDao.deleteById(id);
    }
}