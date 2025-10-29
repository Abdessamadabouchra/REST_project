
package com.project1.project1.controller;
import com.project1.project1.model.User;
import com.project1.project1.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class UserController{

    @Autowired
    private UserService userService;

    @GetMapping("/Users")
    public List<User> GetUsers(){
        return userService.getAllUsers();
    }

}