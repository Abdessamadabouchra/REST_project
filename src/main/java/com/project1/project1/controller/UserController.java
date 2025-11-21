
package com.project1.project1.controller;
import com.project1.project1.dto.ListResponseDto;
import com.project1.project1.dto.UserFullDto;
import com.project1.project1.dto.UserPreviewDto;
import com.project1.project1.services.UserService;
import com.project1.project1.util.GenerateEtag;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.CacheControl;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.time.LocalDate;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

@RestController
@RequestMapping("/v1/users")
public class UserController{

    @Autowired
    private UserService userService;

    @GetMapping
public ResponseEntity<ListResponseDto<UserPreviewDto>> getUsers(
        @RequestParam(required = false) 
        @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDateOfBirth,
        @RequestParam(required = false) 
        @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDateOfBirth,
        @RequestParam(required = false) 
        @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startRegisterDate,
        @RequestParam(required = false) 
        @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endRegisterDate,
        @RequestParam(required = false) String country,
        @RequestParam(required = false) String state,
        @RequestParam(required = false) String city,
        @RequestParam(required = false) String timezone,
        @PageableDefault(sort = "registerDate", direction = Sort.Direction.DESC, size = 10) Pageable pageable,
        @RequestHeader(value = "If-None-Match", required = false) String ifNoneMatch
) {
    ListResponseDto<UserPreviewDto> users = userService.getAllUsers(
            startDateOfBirth, endDateOfBirth, startRegisterDate, endRegisterDate,
            country, state, city, timezone, pageable
    );

    String eTag = GenerateEtag.generate(users);

    if (ifNoneMatch != null && ifNoneMatch.equals(eTag)) {
        return ResponseEntity.status(HttpStatus.NOT_MODIFIED)
                             .eTag(eTag)
                             .build();
    }

    return ResponseEntity.ok()
            .cacheControl(CacheControl.maxAge(60, TimeUnit.SECONDS).cachePublic())
            .eTag(eTag)
            .body(users);
}


    @GetMapping("/{id}")
public ResponseEntity<UserFullDto> getUserById(
        @PathVariable UUID id,
        @RequestHeader(value = "If-None-Match", required = false) String ifNoneMatch
) {
    UserFullDto user = userService.getUserById(id);
    String eTag = GenerateEtag.generate(user);

    if (ifNoneMatch != null && ifNoneMatch.equals(eTag)) {
        return ResponseEntity.status(HttpStatus.NOT_MODIFIED)
                             .eTag(eTag)
                             .build();
    }

    return ResponseEntity.ok()
            .cacheControl(CacheControl.maxAge(60, TimeUnit.SECONDS).cachePublic())
            .eTag(eTag)
            .body(user);
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