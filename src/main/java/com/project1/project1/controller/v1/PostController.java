package com.project1.project1.controller.v1;

import com.project1.project1.dto.*;
import com.project1.project1.exception.BodyNotValidException;
import com.project1.project1.services.PostService;
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
import org.springframework.data.domain.Sort;
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
@RequestMapping(path = "/v1/posts", produces = { MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE })
@Tag(name = "Posts   V1", description = "Operations related to managing user posts, including creation, retrieval, updates, and deletion.")
public class PostController {

    @Autowired
    private PostService postService;

    // GET ALL POSTS
    @Operation(summary = "Get all posts", description = "Retrieves a paginated list of posts with optional filtering by keyword, likes range, and publication dates.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved list of posts"),
            @ApiResponse(responseCode = "304", description = "Resource not modified (ETag matches)")
    })
    @GetMapping()
    public ResponseEntity<ListResponseDto<EntityModel<PostPreviewDto>>> getAllPosts(
            @Parameter(description = "Search keyword for post content")
            @RequestParam(required = false) String keyword,

            @Parameter(description = "Exact text match filter")
            @RequestParam(required = false) String text,

            @Parameter(description = "Minimum number of likes")
            @RequestParam(required = false) Integer minLikes,

            @Parameter(description = "Maximum number of likes")
            @RequestParam(required = false) Integer maxLikes,

            @Parameter(description = "Filter posts published after this date (YYYY-MM-DD)")
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate publishDateAfter,

            @Parameter(description = "Filter posts published before this date (YYYY-MM-DD)")
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate publishDateBefore,

            @Parameter(description = "Pagination and sorting controls")
            @PageableDefault(page = 0, size = 10, sort = "publishDate", direction = Sort.Direction.DESC) Pageable pageable,

            @RequestHeader(value = "If-None-Match", required = false) String ifNoneMatch) {

        ListResponseDto<PostPreviewDto> posts = postService.getAllPosts(keyword, text, minLikes, maxLikes, publishDateAfter, publishDateBefore, pageable);

        String eTag = GenerateEtag.generate(posts);
        if (ifNoneMatch != null && ifNoneMatch.equals(eTag)) {
            return ResponseEntity.status(HttpStatus.NOT_MODIFIED)
                    .eTag(eTag)
                    .build();
        }
        List<EntityModel<PostPreviewDto>> resources = posts.getData().stream().map(PostController::toModelP).toList();
        Page<EntityModel<PostPreviewDto>> modelPage =
                new PageImpl<>(resources, pageable, posts.getTotal());

        return ResponseEntity.ok()
                .cacheControl(CacheControl.maxAge(60, TimeUnit.SECONDS).cachePublic())
                .eTag(eTag)
                .body(new ListResponseDto<>(modelPage));
    }

    // GET POST BY ID
    @Operation(summary = "Get post by ID", description = "Retrieves full details of a specific post by its UUID.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Post found",
                    content = @Content(schema = @Schema(implementation = PostFullDto.class))),
            @ApiResponse(responseCode = "304", description = "Resource not modified"),
            @ApiResponse(responseCode = "404", description = "Post not found")
    })
    @GetMapping("/{postId}")
    public ResponseEntity<EntityModel<PostFullDto>> getPostById(
            @Parameter(description = "Unique identifier of the post")
            @PathVariable UUID postId,

            @RequestHeader(value = "If-None-Match", required = false) String ifNoneMatch) {

        PostFullDto post = postService.getPostById(postId);
        String eTag = GenerateEtag.generate(post);

        if (ifNoneMatch != null && ifNoneMatch.equals(eTag)) {
            return ResponseEntity.status(HttpStatus.NOT_MODIFIED)
                    .eTag(eTag)
                    .build();
        }
        EntityModel<PostFullDto> resource = toModelF(post);
        return ResponseEntity.ok()
                .cacheControl(CacheControl.maxAge(60, TimeUnit.SECONDS).cachePublic())
                .eTag(eTag)
                .body(resource);
    }

    // GET POSTS BY USER
    @Operation(summary = "Get posts by User", description = "Retrieves a paginated list of posts created by a specific user.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved user posts"),
            @ApiResponse(responseCode = "404", description = "User not found")
    })
    @GetMapping("/user/{userId}")
    public ResponseEntity<ListResponseDto<EntityModel<PostPreviewDto>>> getPostsByUser(
            @Parameter(description = "Search keyword")
            @RequestParam(required = false) String keyword,

            @Parameter(description = "Unique identifier of the user")
            @PathVariable UUID userId,

            @RequestParam(required = false) String text,
            @RequestParam(required = false) Integer minLikes,
            @RequestParam(required = false) Integer maxLikes,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate publishDateAfter,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate publishDateBefore,
            @PageableDefault(page = 0, size = 10, sort = "publishDate", direction = Sort.Direction.DESC) Pageable pageable,
            @RequestHeader(value = "If-None-Match", required = false) String ifNoneMatch) {

        ListResponseDto<PostPreviewDto> posts = postService.getPostsByUser(
                keyword, userId, text, minLikes, maxLikes, publishDateAfter, publishDateBefore, pageable);

        String eTag = GenerateEtag.generate(posts);
        if (ifNoneMatch != null && ifNoneMatch.equals(eTag)) {
            return ResponseEntity.status(HttpStatus.NOT_MODIFIED)
                    .eTag(eTag)
                    .build();
        }
        List<EntityModel<PostPreviewDto>> resources = posts.getData().stream().map(PostController::toModelP).toList();
        Page<EntityModel<PostPreviewDto>> modelPage =
                new PageImpl<>(resources, pageable, posts.getTotal());

        return ResponseEntity.ok()
                .cacheControl(CacheControl.maxAge(60, TimeUnit.SECONDS).cachePublic())
                .eTag(eTag)
                .body(new ListResponseDto<>(modelPage));
    }

    // GET POSTS BY TAG
    @Operation(summary = "Get posts by Tags", description = "Retrieves posts associated with one or more tags.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved posts by tags")
    })
    @GetMapping("/tags")
    public ResponseEntity<ListResponseDto<EntityModel<PostPreviewDto>>> getPostsByTag(
            @RequestParam(required = false) String keyword,

            @Parameter(description = "List of tags to filter by (e.g., 'tech', 'java')", required = true)
            @RequestParam List<String> tags,

            @RequestParam(required = false) String text,
            @RequestParam(required = false) Integer minLikes,
            @RequestParam(required = false) Integer maxLikes,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate publishDateAfter,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate publishDateBefore,
            @PageableDefault(page = 0, size = 10, sort = "publishDate", direction = Sort.Direction.DESC) Pageable pageable,
            @RequestHeader(value = "If-None-Match", required = false) String ifNoneMatch) {

        ListResponseDto<PostPreviewDto> posts = postService.getPostsByTag(
                keyword, tags, text, minLikes, maxLikes, publishDateAfter, publishDateBefore, pageable);

        String eTag = GenerateEtag.generate(posts);

        if (ifNoneMatch != null && ifNoneMatch.equals(eTag)) {
            return ResponseEntity.status(HttpStatus.NOT_MODIFIED)
                    .eTag(eTag)
                    .build();
        }
        List<EntityModel<PostPreviewDto>> resources = posts.getData().stream().map(PostController::toModelP).toList();
        Page<EntityModel<PostPreviewDto>> modelPage =
                new PageImpl<>(resources, pageable, posts.getTotal());
        return ResponseEntity.ok()
                .cacheControl(CacheControl.maxAge(60, TimeUnit.SECONDS).cachePublic())
                .eTag(eTag)
                .body(new ListResponseDto<>(modelPage));
    }

    // CREATE POST
    @Operation(summary = "Create a new post", description = "Creates a new post with the provided content and associated owner.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Post created successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid input data"),
            @ApiResponse(responseCode = "404", description = "Owner (User) not found")
    })
    @PostMapping(consumes = { MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE })
    public PostFullDto createPost(
            @Parameter(description = "Post creation payload", required = true)
            @RequestBody @Valid PostCreateDto postCreateDto) {
        return postService.createPost(postCreateDto);
    }

    // UPDATE POST
    @Operation(summary = "Update a post", description = "Updates an existing post. Note: The owner cannot be null.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Post updated successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid input data or missing owner"),
            @ApiResponse(responseCode = "404", description = "Post not found")
    })
    @PutMapping(path = "/{postId}", consumes = { MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE })
    public PostFullDto updatePost(
            @Parameter(description = "Unique identifier of the post to update")
            @PathVariable UUID postId,

            @Parameter(description = "Updated post data", required = true)
            @RequestBody @Valid PostFullDto postUpdateDto) {

        if (postUpdateDto.getOwner() == null) {
            throw new BodyNotValidException("The owner can't be null");
        }
        return postService.updatePost(postId, postUpdateDto);
    }

    // DELETE POST
    @Operation(summary = "Delete a post", description = "Permanently deletes a post by its ID.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Post deleted successfully. Returns the ID of the deleted post."),
            @ApiResponse(responseCode = "404", description = "Post not found")
    })
    @DeleteMapping("/{postId}")
    public UUID deletePost(
            @Parameter(description = "Unique identifier of the post to delete")
            @PathVariable UUID postId) {
        return postService.deletePost(postId);
    }

    public static EntityModel<PostFullDto> toModelF(PostFullDto postDto) {
        return EntityModel.of(postDto,
                linkTo(methodOn(PostController.class).getPostById(postDto.getId(), null)).withSelfRel(),
                WebMvcLinkBuilder.linkTo(methodOn(UserController.class).getUserById(postDto.getOwner().getId(), null)).withRel("owner")
        );
    }

    public static EntityModel<PostPreviewDto> toModelP(PostPreviewDto postDto) {
        return EntityModel.of(postDto,
                linkTo(methodOn(PostController.class).getPostById(postDto.getId(), null)).withSelfRel(),
                linkTo(methodOn(UserController.class).getUserById(postDto.getOwner().getId(), null)).withRel("owner"),
                linkTo(methodOn(CommentController.class).getCommentsByPost(null, postDto.getId(), null, null, null, null)).withRel("comments")
        );
    }
}