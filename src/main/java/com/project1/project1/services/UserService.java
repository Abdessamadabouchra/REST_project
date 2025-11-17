package com.project1.project1.services;

import com.project1.project1.dto.ListResponseDto;
import com.project1.project1.dto.UserPreviewDto;
import com.project1.project1.dto.mappers.UserMapper;
import com.project1.project1.model.User;
import com.project1.project1.repository.UserDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class UserService {

    @Autowired
    private UserDao userDao;
    @Autowired
    UserMapper userMapper;

    public ListResponseDto<UserPreviewDto> getAllUsers(int page, int size, LocalDate dateOfBirth,LocalDate registerDate) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("registerDate").descending());
        Page<UserPreviewDto> userPage = userDao.findAll((Specification<User>) null,pageable).map(userMapper::toPreviewDto);
        return new ListResponseDto<>(userPage);
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

    //ajouter un utilisateur
    public User CreateUser(User user) {
        return userDao.save(user);
    }

}


