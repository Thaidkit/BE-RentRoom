package com.n3c3.rentroom.controller;

import com.n3c3.rentroom.dto.CategoryDTO;
import com.n3c3.rentroom.dto.PostDTO;
import com.n3c3.rentroom.dto.PostSearchDTO;
import com.n3c3.rentroom.dto.UserDTO;
import com.n3c3.rentroom.entity.Post;
import com.n3c3.rentroom.service.PostService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/v1/posts")
public class PostController {

    private final PostService postService;

    public PostController(PostService postService) {
        this.postService = postService;
    }

    @PostMapping()
    public ResponseEntity<?> createPost(@RequestBody PostDTO postDTO) {
        return postService.createPost(postDTO);
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updatePost(@PathVariable Long id, @RequestBody PostDTO postDTO) {
        return postService.updatePost(id, postDTO);
    }

    // Lấy các bài viết chưa đến ngày hết hạn
    @GetMapping
    public ResponseEntity<?> getAllPosts(@RequestParam(defaultValue = "0") int pageNumber,
                                         @RequestParam(defaultValue = "10") int size) {
        return postService.getAllPosts(pageNumber, size);
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getPostById(@PathVariable Long id) {
        return postService.getPostById(id);
    }

    // Search bài đăng đa điều kiện
    @PostMapping("/search")
    public ResponseEntity<?> searchPosts(@RequestBody PostSearchDTO postSearchDTO) {
        return postService.searchPostWithMultiConditions(postSearchDTO);

    }

    // Delete a Post
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deletePost(@PathVariable Long id) {
        return postService.deletePost(id);
    }





}


