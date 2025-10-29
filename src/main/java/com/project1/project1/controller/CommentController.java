package com.project1.project1.controller;


import com.project1.project1.model.Comment;
import com.project1.project1.services.CommentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class CommentController {

    @Autowired
    CommentService commentService;
 @GetMapping("/Comments")
    public Page<Comment> getComments(@PageableDefault(size = 10) Pageable pageable) {
     return commentService.getAllComments(pageable);
 }
}