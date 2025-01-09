package com.n3c3.rentroom.controller;

import com.n3c3.rentroom.service.PostFavoriteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/v1/post-favorite")
public class PostFavoriteController {

    @Autowired
    private PostFavoriteService postFavoriteService;

    @PostMapping("/{postId}/user/{userId}")
    public ResponseEntity<?> saveFavorite(@PathVariable Long userId, @PathVariable Long postId) {
        return postFavoriteService.saveFavorite(userId, postId);
    }

    @DeleteMapping("/{postId}")
    public ResponseEntity<?> removeFavorite(@PathVariable Long postId) {
        return postFavoriteService.removeFavorite(postId);
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<?> getFavoritesByUser(@PathVariable Long userId) {
        return postFavoriteService.getFavoritesByUser(userId);
    }
}
