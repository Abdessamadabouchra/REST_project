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

 @GetMapping
    public ListResponseDto<CommentDto> getComments(
         @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
         @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate,
         @PageableDefault(sort={"publishedDate"},size = 10) Pageable pageable
 ) {
     return commentService.getAllComments(startDate,endDate,pageable);
 }


    @GetMapping("/post/{id}")
    public ListResponseDto<CommentDto> getCommentsByPost(@PathVariable UUID id,
                                               @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
                                               @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate,
                                               @PageableDefault(sort={"publishedDate"},size = 10) Pageable pageable){
        return commentService.getCommentsByPost(id,startDate,endDate,pageable);
    }

    @PostMapping
    public CommentDto createComment(@RequestBody CommentCreateDto commentDto) {
     return commentService.createComment(commentDto);
    }
}