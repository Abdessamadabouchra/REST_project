package com.project1.project1.controller;

import com.project1.project1.dto.ListResponseDto;
import com.project1.project1.dto.PostCreateDto;
import com.project1.project1.dto.PostFullDto;
import com.project1.project1.dto.PostPreviewDto;
import com.project1.project1.services.PostService;

import com.project1.project1.util.GenerateEtag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.CacheControl;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.data.domain.Sort;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

@RestController
@RequestMapping("/v1/posts")
public class PostController {

    @Autowired
    private PostService postService;

    // ================================
    //        GET ALL POSTS
    // ================================
    @GetMapping()
public ResponseEntity<ListResponseDto<PostPreviewDto>> getAllPosts(
        @RequestParam(required = false) String text,
        @RequestParam(required = false) Integer minLikes,
        @RequestParam(required = false) Integer maxLikes,
        @RequestParam(required = false) 
        @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate publishDateAfter,
        @RequestParam(required = false) 
        @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate publishDateBefore,
        @PageableDefault(
                page = 0,
                size = 10,
                sort = "publishDate",
                direction = Sort.Direction.DESC
        ) Pageable pageable,
        @RequestHeader(value = "If-None-Match", required = false) String ifNoneMatch
) {
    ListResponseDto<PostPreviewDto> posts = postService.getAllPosts(text, minLikes, maxLikes, publishDateAfter, publishDateBefore, pageable);

    // Générer un ETag basé sur le hash de la réponse
    String eTag = GenerateEtag.generate(posts);
    System.out.println(eTag);
    // Vérifier si l'ETag du client correspond
    if (ifNoneMatch != null && ifNoneMatch.equals(eTag)) {

        return ResponseEntity.status(HttpStatus.NOT_MODIFIED)
                             .eTag(eTag)
                             .build();

    }

    return ResponseEntity.ok()
            .cacheControl(CacheControl.maxAge(60, TimeUnit.SECONDS).cachePublic())
            .eTag(eTag)
            .body(posts);
}

    // ================================
    //        GET POST BY ID
    // ================================
    @GetMapping("/{postId}")
    public PostFullDto getPostById(@PathVariable UUID postId) {
        return postService.getPostById(postId);
    }

    // ================================
    //        GET POSTS BY USER
    // ================================
    @GetMapping("/user/{userId}")
    public ListResponseDto<PostPreviewDto> getPostsByUser(
            @PathVariable UUID userId,
            @RequestParam(required = false) String text,
            @RequestParam(required = false) Integer minLikes,
            @RequestParam(required = false) Integer maxLikes,
            @RequestParam(required = false) 
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate publishDateAfter,
            @RequestParam(required = false) 
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate publishDateBefore,
            @PageableDefault(
                    page = 0,
                    size = 10,
                    sort = "publishDate",
                    direction = Sort.Direction.DESC
            ) Pageable pageable
    ) {
        return postService.getPostsByUser(userId, text, minLikes, maxLikes, publishDateAfter, publishDateBefore, pageable);
    }

    // ================================
    //        GET POSTS BY TAG
    // ================================
    @GetMapping("/tags")
    public ListResponseDto<PostPreviewDto> getPostsByTag(
            @RequestParam List<String> tags,
            @RequestParam(required = false) String text,
            @RequestParam(required = false) Integer minLikes,
            @RequestParam(required = false) Integer maxLikes,
            @RequestParam(required = false) 
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate publishDateAfter,
            @RequestParam(required = false) 
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate publishDateBefore,
            @PageableDefault(
                    page = 0,
                    size = 10,
                    sort = "publishDate",
                    direction = Sort.Direction.DESC
            ) Pageable pageable
    ) {
        return postService.getPostsByTag(tags, text, minLikes, maxLikes, publishDateAfter, publishDateBefore, pageable);
    }

    // ================================
    //          CREATE POST
    // ================================
    @PostMapping
    public PostFullDto createPost(@RequestBody PostCreateDto postCreateDto) {
        return postService.createPost(postCreateDto);
    }

    // ================================
    //          UPDATE POST
    // ================================
    @PutMapping("/{postId}")
    public PostFullDto updatePost(
            @PathVariable UUID postId,
            @RequestBody PostFullDto postUpdateDto
    ) {
        return postService.updatePost(postId, postUpdateDto);
    }

    // ================================
    //          DELETE POST
    // ================================
    @DeleteMapping("/{postId}")
    public UUID deletePost(@PathVariable UUID postId) {
        return postService.deletePost(postId);
    }
}
