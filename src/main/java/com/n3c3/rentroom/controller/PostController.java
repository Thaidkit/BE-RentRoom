package com.n3c3.rentroom.controller;

import com.n3c3.rentroom.entity.Post;
import com.n3c3.rentroom.service.PostService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/posts")
public class PostController {

    private final PostService postService;

    @Autowired
    public PostController(PostService postService) {
        this.postService = postService;
    }

    // API lấy bài viết theo id
    @GetMapping("/{id}")
    public ResponseEntity<Post> getPost(@PathVariable("id") Long id) {
        Post post = postService.getPostById(id);
        if (post == null) {
            return ResponseEntity.notFound().build();  // Nếu không tìm thấy bài viết
        }
        return ResponseEntity.ok(post);  // Trả về bài viết nếu tìm thấy
    }
}
