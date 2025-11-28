package com.project1.project1.controller.v1;

import com.project1.project1.dto.ListResponseDto;
import com.project1.project1.dto.UserFullDto;
import com.project1.project1.dto.UserPreviewDto;
import com.project1.project1.services.UserService;
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
@RequestMapping(path = "/api/users", produces = { MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE }, headers = "X-API-VERSION=1")
@Tag(name = "Users V1", description = "Operations related to user management, including registration, profile updates, and retrieval.")
public class UserController {

    @Autowired
    private UserService userService;

    // GET ALL USERS
    @Operation(summary = "Get all users", description = "Retrieves a paginated list of users with advanced filtering options (date ranges, location, keyword).")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved list of users"),
            @ApiResponse(responseCode = "304", description = "Resource not modified (ETag matches)"),
            @ApiResponse(responseCode = "400", description = "Invalid request parameters")
    })
    @GetMapping
    public ResponseEntity<ListResponseDto<EntityModel<UserPreviewDto>>> getUsers(
            @Parameter(description = "Keyword to search users by name or email")
            @RequestParam(required = false) String keyword,

            @Parameter(description = "Filter users born after this date (YYYY-MM-DD)")
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDateOfBirth,

            @Parameter(description = "Filter users born before this date (YYYY-MM-DD)")
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDateOfBirth,

            @Parameter(description = "Filter users registered after this date (YYYY-MM-DD)")
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startRegisterDate,

            @Parameter(description = "Filter users registered before this date (YYYY-MM-DD)")
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endRegisterDate,

            @Parameter(description = "Filter by country")
            @RequestParam(required = false) String country,

            @Parameter(description = "Filter by state/region")
            @RequestParam(required = false) String state,

            @Parameter(description = "Filter by city")
            @RequestParam(required = false) String city,

            @Parameter(description = "Filter by timezone (e.g., +01:00)")
            @RequestParam(required = false) String timezone,

            @Parameter(description = "Pagination and sorting (default: sort by registerDate DESC)")
            @PageableDefault(sort = "registerDate", direction = Sort.Direction.DESC, size = 10) Pageable pageable,

            @RequestHeader(value = "If-None-Match", required = false) String ifNoneMatch
    ) {
        ListResponseDto<UserPreviewDto> users = userService.getAllUsers(
                keyword, startDateOfBirth, endDateOfBirth, startRegisterDate, endRegisterDate,
                country, state, city, timezone, pageable
        );

        String eTag = GenerateEtag.generate(users);

        if (ifNoneMatch != null && ifNoneMatch.equals(eTag)) {
            return ResponseEntity.status(HttpStatus.NOT_MODIFIED)
                    .eTag(eTag)
                    .build();
        }

        List<EntityModel<UserPreviewDto>> resources = users.getData().stream().map(UserController::toModelP).toList();
        Page<EntityModel<UserPreviewDto>> modelPage =
                new PageImpl<>(resources, pageable, users.getTotal());

        return ResponseEntity.ok()
                .cacheControl(CacheControl.maxAge(60, TimeUnit.SECONDS).cachePublic())
                .eTag(eTag)
                .body(new ListResponseDto<>(modelPage));
    }

    // GET USER BY ID
    @Operation(summary = "Get user by ID", description = "Retrieves full profile details of a specific user by their UUID.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "User found successfully",
                    content = @Content(schema = @Schema(implementation = UserFullDto.class))),
            @ApiResponse(responseCode = "304", description = "Resource not modified"),
            @ApiResponse(responseCode = "404", description = "User not found")
    })
    @GetMapping("/{id}")
    public ResponseEntity<EntityModel<UserFullDto>> getUserById(
            @Parameter(description = "Unique identifier of the user")
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
        EntityModel<UserFullDto> resource = toModelF(user);
        return ResponseEntity.ok()
                .cacheControl(CacheControl.maxAge(60, TimeUnit.SECONDS).cachePublic())
                .eTag(eTag)
                .body(resource);
    }

    // CREATE USER
    @Operation(summary = "Create a new user", description = "Registers a new user with the provided details.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "User created successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid input data (validation error)")
    })
    @PostMapping(consumes = { MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE })
    public UserFullDto CreateUser(
            @Parameter(description = "User registration payload", required = true)
            @Valid @RequestBody UserFullDto user) {
        return userService.CreateUser(user);
    }

    // UPDATE USER
    @Operation(summary = "Update a user", description = "Updates an existing user's profile. Note: The email field cannot be changed.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "User updated successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid input data or attempt to change immutable fields"),
            @ApiResponse(responseCode = "404", description = "User not found")
    })
    @PutMapping(path = "/{id}", consumes = { MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE })
    public UserFullDto updateUser(
            @Parameter(description = "Unique identifier of the user to update")
            @PathVariable UUID id,

            @Parameter(description = "Updated user profile data", required = true)
            @Valid @RequestBody UserFullDto user) {
        return userService.updateUser(id, user);
    }

    // DELETE USER
    @Operation(summary = "Delete a user", description = "Permanently removes a user account by ID.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "User deleted successfully. Returns the ID of the deleted user."),
            @ApiResponse(responseCode = "404", description = "User not found")
    })
    @DeleteMapping("/{id}")
    public UUID deleteUser(
            @Parameter(description = "Unique identifier of the user to delete")
            @PathVariable UUID id) {
        return userService.deleteUser(id);
    }

    public static EntityModel<UserPreviewDto> toModelP(UserPreviewDto userDto) {
        return EntityModel.of(userDto,
                linkTo(methodOn(UserController.class).getUserById(userDto.getId(), null)).withSelfRel(),
                linkTo(methodOn(PostController.class).getPostsByUser(null, userDto.getId(), null, null, null, null, null, null, null)).withRel("posts"),
                linkTo(methodOn(CommentController.class).getCommentsByUser(null, userDto.getId(), null, null, null, null)).withRel("comments")
        );
    }

    public static EntityModel<UserFullDto> toModelF(UserFullDto userDto) {
        return EntityModel.of(userDto,
                linkTo(methodOn(UserController.class).getUserById(userDto.getId(), null)).withSelfRel(),
                linkTo(methodOn(PostController.class).getPostsByUser(null, userDto.getId(), null, null, null, null, null, null, null)).withRel("user:posts"),
                linkTo(methodOn(CommentController.class).getCommentsByUser(null, userDto.getId(), null, null, null, null)).withRel("user:comments")
        );
    }

}