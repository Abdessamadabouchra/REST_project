
package com.project1.project1.controller;
import com.project1.project1.dto.ListResponseDto;
import com.project1.project1.dto.UserPreviewDto;
import com.project1.project1.model.Comment;
import com.project1.project1.model.User;
import com.project1.project1.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/v1/users")
public class UserController{

    @Autowired
    private UserService userService;

    @GetMapping
    public ListResponseDto<UserPreviewDto> GetUsers(@RequestParam(defaultValue = "0") int page,
                                                    @RequestParam(defaultValue = "10") int size){

        return userService.getAllUsers(page,size);
    }

    @PostMapping
        public User CreateUser(@RequestBody User user){
       return userService.CreateUser(user);
    }
}