package com.project1.project1.services;

import com.project1.project1.dto.ListResponseDto;
import com.project1.project1.dto.UserFullDto;
import com.project1.project1.dto.UserPreviewDto;
import com.project1.project1.dto.mappers.UserMapper;
import com.project1.project1.exception.ResourceNotFoundException;
import com.project1.project1.model.User;
import com.project1.project1.repository.UserDao;
import com.project1.project1.specification.UserSpecification;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;



import java.time.LocalDate;
import java.util.Optional;
import java.util.UUID;

@Service
public class UserService {

    @Autowired
    private UserDao userDao;
    @Autowired
    UserMapper userMapper;

    public ListResponseDto<UserPreviewDto> getAllUsers(int page,int size,LocalDate startDateOfBirth,LocalDate endDateOfBirth,LocalDate startRegisterDate,LocalDate endRegisterDate,String country,String state,String city,String timezone,Pageable pageable) {

        Specification<User> spec= UserSpecification.filter(startDateOfBirth,endDateOfBirth,startRegisterDate,endRegisterDate,country,state,city,timezone);
        Page<UserPreviewDto> userPage = userDao.findAll(spec ,pageable).map(userMapper::toPreviewDto);
        return new ListResponseDto<>(userPage);
    }

    public UserFullDto getUserById(UUID id) {
        return userDao.findById(id).map(userMapper::toFullDto).orElseThrow(()-> new ResourceNotFoundException("No user found!"));
    }

    public User CreateUser(User user) {
        return userDao.save(user);
    }

    public UserFullDto updateUser(UUID id, UserFullDto userdto) {
        User us=userDao.findById(id).orElseThrow(()-> new ResourceNotFoundException("No user found!"));
            userMapper.updateUserFromDto(userdto,us);
            us=userDao.save(us);
            return userMapper.toFullDto(us);
    }

    public UserFullDto deleteUser(UUID id) {
       User us= userDao.findById(id).orElseThrow(()->new ResourceNotFoundException("User not found"));
        userDao.deleteById(id);
       return  userMapper.toFullDto(us);
    }
}


