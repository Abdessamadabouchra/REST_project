package com.project1.project1.controller;

import com.project1.project1.dto.CommentCreateDto;
import com.project1.project1.dto.CommentDto;
import com.project1.project1.dto.ListResponseDto;
import com.project1.project1.model.Comment;
import com.project1.project1.services.CommentService;
import com.project1.project1.util.GenerateEtag;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.hateoas.EntityModel;
import org.springframework.http.CacheControl;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@RestController
@RequestMapping(path = "v1/comments",produces = { MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE })
public class CommentController {

    @Autowired
    CommentService commentService;

    // get list
    @GetMapping
    public ResponseEntity<ListResponseDto<CommentDto>> getComments(
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate,
            @PageableDefault(sort = { "publishDate" }, size = 10) Pageable pageable,
            @RequestHeader(value = "If-None-Match", required = false) String ifNoneMatch) {
        ListResponseDto<CommentDto> comments = commentService.getAllComments(startDate, endDate, pageable);
        String eTag = GenerateEtag.generate(comments);

        if (ifNoneMatch != null && ifNoneMatch.equals(eTag)) {
            return ResponseEntity.status(HttpStatus.NOT_MODIFIED)
                    .eTag(eTag)
                    .build();
        }
        return ResponseEntity.ok()
                .cacheControl(CacheControl.maxAge(60, TimeUnit.SECONDS).cachePublic())
                .eTag(eTag)
                .body(comments);
    }

    @GetMapping("{id}")
    public ResponseEntity<EntityModel<CommentDto>> getCommentById(
            @PathVariable UUID id,
            @RequestHeader(value = "If-None-Match", required = false) String ifNoneMatch) {
        CommentDto commentDto = commentService.getCommentById(id);
        String eTag = GenerateEtag.generate(commentDto);

        if (ifNoneMatch != null && ifNoneMatch.equals(eTag)) {
            return ResponseEntity.status(HttpStatus.NOT_MODIFIED)
                    .eTag(eTag)
                    .build();
        }
        EntityModel<CommentDto> resource = toModel(commentDto);

        return ResponseEntity.ok()
                .cacheControl(CacheControl.maxAge(60, TimeUnit.SECONDS).cachePublic())
                .eTag(eTag)
                .body(resource);
    }

    // get comments by post
    @GetMapping("/post/{id}")
    public ResponseEntity<ListResponseDto<CommentDto>> getCommentsByPost(
            @PathVariable UUID id,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate,
            @PageableDefault(sort = { "publishDate" }, size = 10) Pageable pageable,
            @RequestHeader(value = "If-None-Match", required = false) String ifNoneMatch) {
        ListResponseDto<CommentDto> comments = commentService.getCommentsByPost(id, startDate, endDate, pageable);
        String eTag = GenerateEtag.generate(comments);

        if (ifNoneMatch != null && ifNoneMatch.equals(eTag)) {
            return ResponseEntity.status(HttpStatus.NOT_MODIFIED)
                    .eTag(eTag)
                    .build();
        }

        return ResponseEntity.ok()
                .cacheControl(CacheControl.maxAge(60, TimeUnit.SECONDS).cachePublic())
                .eTag(eTag)
                .body(comments);
    }

    @GetMapping("/user/{id}")
    public ResponseEntity<ListResponseDto<CommentDto>> getCommentsByUser(
            @PathVariable UUID id,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate,
            @PageableDefault(sort = { "publishDate" }, size = 10) Pageable pageable,
            @RequestHeader(value = "If-None-Match", required = false) String ifNoneMatch) {
        ListResponseDto<CommentDto> comments = commentService.getCommentsByUser(id, startDate, endDate, pageable);
        String eTag = GenerateEtag.generate(comments);

        if (ifNoneMatch != null && ifNoneMatch.equals(eTag)) {
            return ResponseEntity.status(HttpStatus.NOT_MODIFIED)
                    .eTag(eTag)
                    .build();
        }

        return ResponseEntity.ok()
                .cacheControl(CacheControl.maxAge(60, TimeUnit.SECONDS).cachePublic())
                .eTag(eTag)
                .body(comments);
    }

    // create comment
    @PostMapping(consumes = { MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE })
    public CommentDto createComment(@RequestBody @Valid CommentCreateDto commentDto) {
        return commentService.createComment(commentDto);
    }

    // delete
    @DeleteMapping("/{id}")
    public UUID deleteComment(@PathVariable UUID id) {
        return commentService.deleteComment(id);
    }

    public static EntityModel<CommentDto> toModel(CommentDto commentDto) {
        return EntityModel.of(commentDto,
        linkTo(methodOn(CommentController.class).getCommentById(commentDto.getId(), null)).withSelfRel(),
                linkTo(methodOn(UserController.class).getUserById(commentDto.getUser().getId(),null)).withRel("user"),
                linkTo(methodOn(PostController.class).getPostById(commentDto.getPostId(),null)).withRel("post")
        );}

}