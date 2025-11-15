package com.project1.project1.controller;


import com.project1.project1.dto.CommentDto;
import com.project1.project1.dto.ListResponseDto;
import com.project1.project1.model.Comment;
import com.project1.project1.services.CommentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;
import java.util.UUID;

@RestController
public class CommentController {

    @Autowired
    CommentService commentService;
 @GetMapping("/Comments")
    public ListResponseDto<CommentDto> getComments(@PageableDefault(sort={"publishedDate"}) Pageable pageable) {
     return commentService.getAllComments(pageable);
 }

    @GetMapping("/comments/{id}")
    public Optional<Comment> getCommentById(@PathVariable UUID id){
        return commentService.getCommentById(id);
    }
}