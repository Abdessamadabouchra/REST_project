package com.project1.project1.controller;


import com.project1.project1.dto.CommentCreateDto;
import com.project1.project1.dto.CommentDto;
import com.project1.project1.dto.ListResponseDto;
import com.project1.project1.model.Comment;
import com.project1.project1.services.CommentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.Optional;
import java.util.UUID;

@RestController
@RequestMapping("v1/comments")
public class CommentController {

    @Autowired
    CommentService commentService;

    //get list
 @GetMapping
    public ListResponseDto<CommentDto> getComments(
         @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
         @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate,
         @PageableDefault(sort={"publishDate"},size = 10) Pageable pageable
 ) {
     return commentService.getAllComments(startDate,endDate,pageable);
 }

    //Get by id
    @GetMapping("{id}")
    public Comment getCommentById(@PathVariable UUID id) {
        return commentService.getCommentById(id);
    }

 //get comments by post
    @GetMapping("/post/{id}")
    public ListResponseDto<CommentDto> getCommentsByPost(@PathVariable UUID id,
                                               @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
                                               @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate,
                                               @PageableDefault(sort={"publishDate"},size = 10) Pageable pageable){
        return commentService.getCommentsByPost(id,startDate,endDate,pageable);
    }

    //get comments by user
    @GetMapping("/user/{id}")
    public ListResponseDto<CommentDto> getCommentsByUser(@PathVariable UUID id,
                                                         @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
                                                         @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate,
                                                         @PageableDefault(sort={"publishDate"},size = 10) Pageable pageable){
        return commentService.getCommentsByUser(id,startDate,endDate,pageable);
    }

    //create comment
    @PostMapping
    public CommentDto createComment(@RequestBody CommentCreateDto commentDto) {
     return commentService.createComment(commentDto);
    }

    //delete
    @DeleteMapping("/{id}")
    public UUID deleteComment(@PathVariable UUID id) {
       return commentService.deleteComment(id);
    }
}