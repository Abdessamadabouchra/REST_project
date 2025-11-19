
package com.project1.project1.controller;
import com.project1.project1.dto.ListResponseDto;
import com.project1.project1.dto.UserFullDto;
import com.project1.project1.dto.UserPreviewDto;
import com.project1.project1.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;
import java.time.LocalDate;
import java.util.UUID;

@RestController
@RequestMapping("/v1/users")
public class UserController{

    @Autowired
    private UserService userService;

    @GetMapping
    public ListResponseDto<UserPreviewDto> GetUsers(
                                                    @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDateOfBirth,
                                                    @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDateOfBirth,
                                                    @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startRegisterDate,
                                                    @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endRegisterDate,
                                                    @RequestParam(required = false) String country,
                                                    @RequestParam(required = false) String state,
                                                    @RequestParam(required = false) String city,
                                                    @RequestParam(required = false) String timezone,
                                                    @PageableDefault(sort = "registerDate", direction = Sort.Direction.DESC,size=10) Pageable pageable
    ){
        return userService.getAllUsers(startDateOfBirth,endDateOfBirth,startRegisterDate,endRegisterDate,country,state,city,timezone,pageable);
    }

    @GetMapping("/{id}")
    public UserFullDto getUserByID(@PathVariable UUID id) {
        return userService.getUserById(id);
    }

    @PostMapping
        public UserFullDto CreateUser(@RequestBody UserFullDto user){
       return userService.CreateUser(user);
    }

    @PutMapping("/{id}")
    public UserFullDto updateUser(@PathVariable UUID id,@RequestBody UserFullDto user){
        return userService.updateUser(id,user);
    }

    @DeleteMapping("/{id}")
    public UUID deleteUser(@PathVariable UUID id){
       return  userService.deleteUser(id);
    }

}