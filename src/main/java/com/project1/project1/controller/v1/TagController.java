package com.project1.project1.controller.v1;

import java.util.List;
import java.util.concurrent.TimeUnit;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.CacheControl;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.project1.project1.services.TagService;
import com.project1.project1.util.GenerateEtag;

@RestController
@RequestMapping(path = "/api/tags", produces = { MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE }, headers = "X-API-VERSION=1")
@Tag(name = "Tags   V1", description = "Operations related to retrieving available tags used in posts.")
public class TagController {

    @Autowired
    private TagService tagService;

    @Operation(summary = "Get all tags", description = "Retrieves a list of all unique tags currently used in the system. Useful for autocomplete or filtering.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved list of tags",
                    content = @Content(array = @ArraySchema(schema = @Schema(implementation = String.class, example = "tech")))),
            @ApiResponse(responseCode = "304", description = "Resource not modified (ETag matches)")
    })
    @GetMapping()
    public ResponseEntity<List<String>> getTags(
            @Parameter(description = "ETag value from previous request for conditional caching")
            @RequestHeader(value = "If-None-Match", required = false) String ifNoneMatch) {

        List<String> tags = tagService.getAllTags();
        String eTag = GenerateEtag.generate(tags);

        if (ifNoneMatch != null && ifNoneMatch.equals(eTag)) {
            return ResponseEntity.status(HttpStatus.NOT_MODIFIED)
                    .eTag(eTag)
                    .build();
        }

        return ResponseEntity.ok()
                .cacheControl(CacheControl.maxAge(60, TimeUnit.SECONDS).cachePublic())
                .eTag(eTag)
                .body(tags);
    }
}