package com.n3c3.rentroom.controller;

import com.n3c3.rentroom.dto.CommentCreateDTO;
import com.n3c3.rentroom.service.CommentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/comments")
public class CommentController {
    @Autowired
    private CommentService commentService;

    @PostMapping
    public ResponseEntity<?> createComment(@RequestBody CommentCreateDTO commentCreateDTO) {
        return commentService.createComment(commentCreateDTO);
    }


}
