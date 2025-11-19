package com.project1.project1.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.project1.project1.services.TagService;

@RestController
@RequestMapping("/v1/tags")
public class TagController {

    @Autowired
    private TagService tagService;

    @GetMapping
    public List<String> getTags() {
        return tagService.getAllTags();
    }
}
