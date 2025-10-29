package com.project1.project1.services;
import com.project1.project1.model.Comment;
import com.project1.project1.repository.CommentDao;  
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import java.util.List;
    
import java.util.Optional;          


@Service
public class CommentService {
    @Autowired
    private CommentDao commentDao;

    public Page<Comment> getAllComments(Pageable pagable) {
        Page<Comment> CommentPage= commentDao.findAll(pagable);
        return CommentPage;
    }

    public Optional<Comment> getCommentById(Integer id) {
        return commentDao.findById(id);
    }

    public Comment createComment(Comment comment) {
        return commentDao.save(comment);
    }

    public void deleteComment(Integer id) {
        commentDao.deleteById(id);
    }
}   