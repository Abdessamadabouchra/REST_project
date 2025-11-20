package com.project1.project1.services;
import com.project1.project1.dto.CommentCreateDto;
import com.project1.project1.dto.CommentDto;
import com.project1.project1.dto.ListResponseDto;
import com.project1.project1.dto.PostFullDto;
import com.project1.project1.dto.mappers.CommentMapper;
import com.project1.project1.model.Comment;
import com.project1.project1.repository.CommentDao;
import com.project1.project1.repository.PostDao;
import com.project1.project1.specification.CommentSpecification;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
    
import java.util.Optional;
import java.util.UUID;


@Service
public class CommentService {
    @Autowired
    private CommentDao commentDao;
    @Autowired
    private CommentMapper commentMapper;
    @Autowired
    private PostService postService;

    public ListResponseDto<CommentDto> getAllComments(LocalDate startDate,LocalDate endDate,Pageable pageable) {
        Specification<Comment> spec= CommentSpecification.filter(startDate,endDate);
        Page<CommentDto> CommentPage= commentDao.findAll(spec,pageable).map(commentMapper::toDto);

        return new ListResponseDto<>(CommentPage);
    }

    public ListResponseDto<CommentDto> getCommentsByPost(UUID id,LocalDate startDate,LocalDate endDate,Pageable pageable) {
        Specification<Comment> spec= CommentSpecification.byPost(id).and(CommentSpecification.filter(startDate,endDate));
        Page<CommentDto> comments=commentDao.findAll(spec,pageable).map(commentMapper::toDto);
        return new ListResponseDto<>(comments);
    }

    public CommentDto createComment(CommentCreateDto commentDto) {
        if(commentDto.getPost()==null & commentDto.getOwner()==null) {
            throw new IllegalArgumentException("Post Id and User Id must not be null");
        }
        Comment c=commentMapper.toEntity(commentDto);
        return commentMapper.toDto(commentDao.save(c)) ;
    }

    public void deleteComment(UUID id) {
        commentDao.deleteById(id);
    }
}   