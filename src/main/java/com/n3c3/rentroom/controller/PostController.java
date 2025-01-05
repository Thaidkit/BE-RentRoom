package com.n3c3.rentroom.controller;

import com.n3c3.rentroom.dto.*;
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
    public ResponseEntity<?> createPost(@RequestBody PostCreateDTO postDTO) {
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

    // api lấy bài viết của 1 user
    @GetMapping("/user/{id}")
    public ResponseEntity<?> getPostByUserId(@PathVariable Long id,
                                             @RequestParam(defaultValue = "0") int pageNumber,
                                             @RequestParam(defaultValue = "10") int size){
        return postService.getALLPostByUserId(id, pageNumber, size);
    }

    // Search bài đăng đa điều kiện
    @PostMapping("/search-filter")
    public ResponseEntity<?> searchPosts(@RequestBody PostSearchDTO postSearchDTO) {
        return postService.searchPostWithMultiConditions(postSearchDTO);
    }

    @GetMapping("/search")
    public ResponseEntity<?> searchPosts(
            @RequestParam String keyword,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        return postService.searchPostByKeyword(keyword, page, size);
    }

    @GetMapping("/search-map")
    public ResponseEntity<?> getAllAddressByKeyWord(@RequestParam(defaultValue = "") String keyword) {
        return postService.getAllAddress(keyword);
    }

    // Delete a Post
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deletePost(@PathVariable Long id) {
        return postService.deletePost(id);
    }
}


