package com.n3c3.rentroom.controller;

import com.n3c3.rentroom.dto.CategoryDTO;
import com.n3c3.rentroom.dto.PostDTO;
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
@RequestMapping("/api/posts")
public class PostController {

    private final PostService postService;

    @Autowired
    public PostController(PostService postService) {
        this.postService = postService;
    }

//    // Create a new Post
//@PostMapping
//public ResponseEntity<PostDTO> createPost(@RequestBody PostDTO postDTO) {
//    // Tạo bài viết mới thông qua service
//    Post post = postService.createPost(postDTO);
//
//    // Chuyển đổi từ Post sang PostDTO để trả về
//    UserDTO userDTO = new UserDTO(post.getUser().getId());
//    CategoryDTO categoryDTO = new CategoryDTO(post.getCategory().getId());
//    PostDTO responseDTO = new PostDTO(
//            post.getId(),
//            post.getTitle(),
//            post.getAddress(),
//            post.getPrice(),
//            post.getRoomSize(),
//            post.getDescription(),
//            post.getImages(),
//            post.getVideos(),
//            post.getExpiredDate().toString(),
//            userDTO,
//            categoryDTO
//    );
//
//    return ResponseEntity.ok(responseDTO);
//}



    // Get all Posts
    @GetMapping
    public ResponseEntity<List<PostDTO>> getAllPosts() {
        // Lấy danh sách tất cả bài viết từ service
        List<PostDTO> postDTOs = postService.getAllPosts().stream()
                .map(post -> {
                    // Tạo UserDTO và CategoryDTO từ đối tượng User và Category
                    UserDTO userDTO = new UserDTO(post.getUser().getId());
                    CategoryDTO categoryDTO = new CategoryDTO(post.getCategory().getId());

                    // Tạo PostDTO
                    return new PostDTO(
                            post.getId(),
                            post.getTitle(),
                            post.getAddress(),
                            post.getPrice(),
                            post.getRoomSize(),
                            post.getDescription(),
                            post.getImages(),
                            post.getVideos(),
                            post.getExpiredDate().toString(),
                            userDTO,
                            categoryDTO
                    );
                })
                .collect(Collectors.toList());

        return ResponseEntity.ok(postDTOs);
    }



    // Get post by id
    @GetMapping("/{id}")
    public ResponseEntity<PostDTO> getPost(@PathVariable("id") Long id) {
        Post post = postService.getPostById(id);
        if (post == null) {
            return ResponseEntity.notFound().build();  // Nếu không tìm thấy bài viết
        }

        // Tạo UserDTO và CategoryDTO từ đối tượng User và Category
        UserDTO userDTO = new UserDTO(post.getUser().getId());
        CategoryDTO categoryDTO = new CategoryDTO(post.getCategory().getId());

        // Tạo PostDTO
        PostDTO postDTO = new PostDTO(post.getId(), post.getTitle(), post.getAddress(), post.getPrice(),
                post.getRoomSize(), post.getDescription(), post.getImages(),
                post.getVideos(), post.getExpiredDate().toString(), userDTO, categoryDTO);

        return ResponseEntity.ok(postDTO);
    }

//    // Update a Post
//    @PutMapping("/{id}")
//    public ResponseEntity<Post> updatePost(@PathVariable Long id, @RequestBody Post postDetails) {
//        Post updatedPost = postService.updatePost(id, postDetails);
//        return ResponseEntity.ok(updatedPost);
//    }

    // Delete a Post
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deletePost(@PathVariable("id") Long id) {
        try {
            postService.deletePost(id);
            return ResponseEntity.ok("Xóa bài viết thành công.");
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }



}


