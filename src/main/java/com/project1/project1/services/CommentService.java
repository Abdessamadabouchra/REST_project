package com.project1.project1.services;
import com.project1.project1.dto.CommentDto;
import com.project1.project1.dto.ListResponseDto;
import com.project1.project1.dto.mappers.CommentMapper;
import com.project1.project1.model.Comment;
import com.project1.project1.repository.CommentDao;  
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import java.util.List;
    
import java.util.Optional;
import java.util.UUID;


@Service
public class CommentService {
    @Autowired
    private CommentDao commentDao;
    @Autowired
    private CommentMapper commentMapper;

    public ListResponseDto<CommentDto> getAllComments(Pageable pagable) {
        Page<CommentDto> CommentPage= commentDao.findAll(pagable).map(commentMapper::commentToCommentDto);

        return new ListResponseDto<>(CommentPage);
    }

    public Optional<Comment> getCommentById(UUID id) {
        return commentDao.findById(id);
    }

    public Comment createComment(Comment comment) {
        return commentDao.save(comment);
    }

    public void deleteComment(UUID id) {
        commentDao.deleteById(id);
    }
}   