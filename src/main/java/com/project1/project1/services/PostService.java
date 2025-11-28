package com.project1.project1.services;
import com.project1.project1.dto.ListResponseDto;
import com.project1.project1.dto.PostCreateDto;
import com.project1.project1.dto.PostFullDto;
import com.project1.project1.dto.PostPreviewDto;
import com.project1.project1.dto.mappers.PostMapper;
import com.project1.project1.exception.InvalidOperationException;
import com.project1.project1.exception.ResourceNotFoundException;
import com.project1.project1.model.Post;
import com.project1.project1.model.User;
import com.project1.project1.repository.PostDao;
import com.project1.project1.repository.UserDao;
import com.project1.project1.specification.PostSpecifications;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@Service
public class PostService {
  @Autowired
    private PostDao postDao;
  @Autowired
  private UserDao userdao;
    @Autowired
    private PostMapper postMapper;

//   ========= GET ALL POSTS =========
    public ListResponseDto<PostPreviewDto> getAllPosts(
            String keyword,
            String text,
            Integer minLikes,
            Integer maxLikes,
            LocalDate publishDateAfter,
            LocalDate publishDateBefore,
            Pageable pageable
    ) {
        Specification<Post> spec = PostSpecifications.search(keyword).and(
                PostSpecifications.filter(
                 text, minLikes, maxLikes, publishDateAfter, publishDateBefore
        ));
        Page<PostPreviewDto> dtoPage =
                postDao.findAll(spec, pageable).map(postMapper::toPreviewDto);

        return new ListResponseDto<>(dtoPage);
    }

   // ========= GET POST BY ID =========
public PostFullDto getPostById(UUID postId) {
    if (postId == null) {
        throw new IllegalArgumentException("postId must not be null");
    }
    Post post = postDao.findById(postId)
            .orElseThrow(() -> new ResourceNotFoundException("Post not found"));

    return postMapper.toFullDto(post);
}

    // GET LIST BY USER

   public ListResponseDto<PostPreviewDto> getPostsByUser(
           String keyword,
            UUID userId,
            String text,
            Integer minLikes,
            Integer maxLikes,
            LocalDate publishDateAfter,
            LocalDate publishDateBefore,
            Pageable pageable
    ) {

        Specification<Post> spec =
                PostSpecifications.byUser(userId).and(
                        PostSpecifications.search(keyword).and(PostSpecifications.filter(
                             text, minLikes, maxLikes, publishDateAfter, publishDateBefore
                        )));

        Page<PostPreviewDto> dtoPage = postDao
                .findAll(spec, pageable)
                .map(postMapper::toPreviewDto);

        return new ListResponseDto<>(dtoPage);
    }

    // ========= GET POSTS BY TAG =========
public ListResponseDto<PostPreviewDto> getPostsByTag(
        String keyword,
        List<String> tags,
        String text,
        Integer minLikes,
        Integer maxLikes,
        LocalDate publishDateAfter,
        LocalDate publishDateBefore,
        Pageable pageable
) {

  Specification<Post> spec = PostSpecifications.byTags(tags).and(
          PostSpecifications.search(keyword).and(PostSpecifications.filter(
                text, minLikes, maxLikes, publishDateAfter, publishDateBefore
        )));

    Page<PostPreviewDto> dtoPage = postDao.findAll(spec, pageable)
            .map(postMapper::toPreviewDto);

    return new ListResponseDto<>(dtoPage);
}
 // ========= CREATE POST =========
    public PostFullDto createPost(PostCreateDto postCreateDto) {
        User owner = userdao.findById(postCreateDto.getOwner())
                .orElseThrow(() -> new ResourceNotFoundException("Owner not found"));
        Post post = postMapper.toEntity(postCreateDto);
        post.setOwner(owner);

        String slug = postCreateDto.getText()
                    .toLowerCase()
                    .replaceAll("[^a-z0-9]+", "-");

        String uniqueId = UUID.randomUUID().toString().substring(0, 8);
        post.setLink(slug + "-" + uniqueId);
        Post savedPost = postDao.save(post);
        return postMapper.toFullDto(savedPost);
    }

//  // ========= UPDATE POST =========

    public PostFullDto updatePost(UUID postId, PostFullDto postUpdateDto) {// Récupère le post existant
        Post post = postDao.findById(postId)
                .orElseThrow(() -> new ResourceNotFoundException("Post not found"));


        if (postUpdateDto.getOwner().getId() != null && !postUpdateDto.getOwner().getId().equals(post.getOwner().getId())) {
            throw new InvalidOperationException("Owner cannot be changed");
        }

        postMapper.updatePostDto(postUpdateDto, post);
        Post updatedPost = postDao.save(post);
        return postMapper.toFullDto(updatedPost);
    }

 // ========= DELETE POST =========
    public UUID deletePost(UUID postId) {
        Post post = postDao.findById(postId)
                .orElseThrow(() -> new ResourceNotFoundException("Post not found"));

        postDao.delete(post);
        return postId;
    }

}
