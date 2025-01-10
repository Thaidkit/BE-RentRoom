package com.n3c3.rentroom.service;

import com.n3c3.rentroom.dto.CommentCreateDTO;
import com.n3c3.rentroom.dto.ObjectResponse;
import com.n3c3.rentroom.entity.Comment;
import com.n3c3.rentroom.entity.User;
import com.n3c3.rentroom.repository.CommentRepository;
import com.n3c3.rentroom.repository.PostRepository;
import com.n3c3.rentroom.repository.UserRepository;
import org.hibernate.JDBCException;
import org.hibernate.exception.JDBCConnectionException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.logging.Logger;

@Service
public class CommentService {

    private final Logger log = Logger.getLogger(this.getClass().getName());

    private final UserRepository userRepository;
    private final PostRepository postRepository;
    private final CommentRepository commentRepository;

    public CommentService(UserRepository userRepository,
                          PostRepository postRepository,
                          CommentRepository commentRepository) {
        this.userRepository = userRepository;
        this.postRepository = postRepository;
        this.commentRepository = commentRepository;
    }

    @Transactional
    public ResponseEntity<?> createComment(CommentCreateDTO commentCreateDTO) {
        try {
            String comment = commentCreateDTO.getComment();
            Long userId = commentCreateDTO.getUserId();
            Long postId = commentCreateDTO.getPostId();

            userRepository.findById(userId).orElseThrow(() -> new RuntimeException("User Not Found"));
            postRepository.findById(postId).orElseThrow(() -> new RuntimeException("Post Not Found"));

            commentRepository.saveComment(userId, postId, comment);
            log.info("Comment created");
            return ResponseEntity.ok(new ObjectResponse(200, "Comment saved successfully!", ""));
        }catch (Exception e) {
            log.warning("Error saving comment: " + e.getMessage());
            return ResponseEntity.status(400).body(new ObjectResponse(400, "Error saving comment", e.getMessage()));
        }
    }

}
