package com.project1.project1.controller.v2;


import com.project1.project1.controller.v1.CommentController;
import com.project1.project1.controller.v1.PostController;
import com.project1.project1.controller.v1.UserController;
import com.project1.project1.dto.CommentCreateDto;
import com.project1.project1.dto.CommentDto;
import com.project1.project1.dto.ListResponseDto;
import com.project1.project1.services.CommentService;
import com.project1.project1.util.GenerateEtag;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder;
import org.springframework.http.CacheControl;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@RestController
@RequestMapping(path = "/api/v2/comments",produces = { MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE })
@Tag(name = "Comments  V2", description = "Operations related to comment management, including retrieval, creation, and deletion version 2.")
public class CommentControllerV2 {

    @Autowired
    CommentService commentService;

    // get list
    @Operation(summary = "Get all comments", description = "Retrieves a paginated list of comments with optional filtering by keyword and date range. Supports ETag for caching.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved list of comments"),
            @ApiResponse(responseCode = "304", description = "Resource not modified (ETag matches)"),
            @ApiResponse(responseCode = "400", description = "Invalid request parameters")
    })
    @GetMapping
    public ResponseEntity<ListResponseDto<EntityModel<CommentDto>>> getComments(
            @Parameter(description = "Keyword to search within comment content")
            @RequestParam(required = false) String keyword,

            @Parameter(description = "Filter comments created after this date (ISO format: YYYY-MM-DD)")
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,

            @Parameter(description = "Filter comments created before this date (ISO format: YYYY-MM-DD)")
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate,

            @Parameter(description = "Pagination and sorting information (e.g., page=0, size=10, sort=publishDate,desc)")
            @PageableDefault(sort = { "publishDate" }, size = 10) Pageable pageable,

            @Parameter(description = "ETag value from previous request for conditional requests")
            @RequestHeader(value = "If-None-Match", required = false) String ifNoneMatch) {

        ListResponseDto<CommentDto> comments = commentService.getAllComments(keyword, startDate, endDate, pageable);
        String eTag = GenerateEtag.generate(comments);

        if (ifNoneMatch != null && ifNoneMatch.equals(eTag)) {
            return ResponseEntity.status(HttpStatus.NOT_MODIFIED)
                    .eTag(eTag)
                    .build();
        }
        List<EntityModel<CommentDto>> resources = comments.getData().stream().map(com.project1.project1.controller.v1.CommentController::toModel).toList();
        Page<EntityModel<CommentDto>> modelPage =
                new PageImpl<>(resources, pageable, comments.getTotal());
        return ResponseEntity.ok()
                .cacheControl(CacheControl.maxAge(60, TimeUnit.SECONDS).cachePublic())
                .eTag(eTag)
                .body(new ListResponseDto<>(modelPage));
    }

    @Operation(summary = "Get comment by ID", description = "Retrieves a single comment by its unique UUID.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Comment found",
                    content = @Content(schema = @Schema(implementation = CommentDto.class))),
            @ApiResponse(responseCode = "304", description = "Resource not modified"),
            @ApiResponse(responseCode = "404", description = "Comment not found")
    })
    @GetMapping("{id}")
    public ResponseEntity<EntityModel<CommentDto>> getCommentById(
            @Parameter(description = "Unique identifier of the comment")
            @PathVariable UUID id,

            @Parameter(description = "ETag value")
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
    @Operation(summary = "Get comments by Post", description = "Retrieves a paginated list of comments associated with a specific Post ID.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved comments for the post"),
            @ApiResponse(responseCode = "404", description = "Post not found")
    })
    @GetMapping("/post/{id}")
    public ResponseEntity<ListResponseDto<EntityModel<CommentDto>>> getCommentsByPost(
            @Parameter(description = "Keyword search")
            @RequestParam(required = false) String keyword,

            @Parameter(description = "Unique identifier of the Post")
            @PathVariable UUID id,

            @Parameter(description = "Start date filter (YYYY-MM-DD)")
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,

            @Parameter(description = "End date filter (YYYY-MM-DD)")
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate,

            @Parameter(description = "Pagination controls")
            @PageableDefault(sort = { "publishDate" }, size = 10) Pageable pageable,

            @RequestHeader(value = "If-None-Match", required = false) String ifNoneMatch) {

        ListResponseDto<CommentDto> comments = commentService.getCommentsByPost(keyword, id, startDate, endDate, pageable);
        String eTag = GenerateEtag.generate(comments);

        if (ifNoneMatch != null && ifNoneMatch.equals(eTag)) {
            return ResponseEntity.status(HttpStatus.NOT_MODIFIED)
                    .eTag(eTag)
                    .build();
        }
        List<EntityModel<CommentDto>> resources = comments.getData().stream().map(com.project1.project1.controller.v1.CommentController::toModel).toList();
        Page<EntityModel<CommentDto>> modelPage =
                new PageImpl<>(resources, pageable, comments.getTotal());
        return ResponseEntity.ok()
                .cacheControl(CacheControl.maxAge(60, TimeUnit.SECONDS).cachePublic())
                .eTag(eTag)
                .body(new ListResponseDto<>(modelPage));
    }

    @Operation(summary = "Get comments by User", description = "Retrieves a paginated list of comments created by a specific User ID.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved comments for the user"),
            @ApiResponse(responseCode = "404", description = "User not found")
    })
    @GetMapping("/user/{id}")
    public ResponseEntity<ListResponseDto<EntityModel<CommentDto>>> getCommentsByUser(
            @Parameter(description = "Keyword search")
            @RequestParam(required = false) String keyword,

            @Parameter(description = "Unique identifier of the User")
            @PathVariable UUID id,

            @Parameter(description = "Start date filter")
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,

            @Parameter(description = "End date filter")
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate,

            @Parameter(description = "Pagination controls")
            @PageableDefault(sort = { "publishDate" }, size = 10) Pageable pageable,

            @RequestHeader(value = "If-None-Match", required = false) String ifNoneMatch) {

        ListResponseDto<CommentDto> comments = commentService.getCommentsByUser(keyword, id, startDate, endDate, pageable);
        String eTag = GenerateEtag.generate(comments);

        if (ifNoneMatch != null && ifNoneMatch.equals(eTag)) {
            return ResponseEntity.status(HttpStatus.NOT_MODIFIED)
                    .eTag(eTag)
                    .build();
        }
        List<EntityModel<CommentDto>> resources = comments.getData().stream().map(com.project1.project1.controller.v1.CommentController::toModel).toList();
        Page<EntityModel<CommentDto>> modelPage =
                new PageImpl<>(resources, pageable, comments.getTotal());
        return ResponseEntity.ok()
                .cacheControl(CacheControl.maxAge(60, TimeUnit.SECONDS).cachePublic())
                .eTag(eTag)
                .body(new ListResponseDto<>(modelPage));
    }

    // create comment
    @Operation(summary = "Create a new comment", description = "Creates a new comment for a post. Returns the created comment details.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Comment created successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid input data (validation error)"),
            @ApiResponse(responseCode = "404", description = "Related Post or User not found")
    })
    @PostMapping(consumes = { MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE })
    public CommentDto createComment(
            @Parameter(description = "Comment creation payload", required = true)
            @RequestBody @Valid CommentCreateDto commentDto) {
        return commentService.createComment(commentDto);
    }

    // delete
    @Operation(summary = "Delete a comment", description = "Permanently removes a comment by its ID.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Comment deleted successfully. Returns the ID of the deleted comment."),
            @ApiResponse(responseCode = "404", description = "Comment not found")
    })
    @DeleteMapping("/{id}")
    public UUID deleteComment(
            @Parameter(description = "Unique identifier of the comment to be deleted")
            @PathVariable UUID id) {
        return commentService.deleteComment(id);
    }

    public static EntityModel<CommentDto> toModel(CommentDto commentDto) {
        return EntityModel.of(commentDto,
                linkTo(methodOn(com.project1.project1.controller.v1.CommentController.class).getCommentById(commentDto.getId(), null)).withSelfRel(),
                WebMvcLinkBuilder.linkTo(methodOn(UserController.class).getUserById(commentDto.getUser().getId(), null)).withRel("owner"),
                WebMvcLinkBuilder.linkTo(methodOn(PostController.class).getPostById(commentDto.getPostId(), null)).withRel("post")
        );
    }

}