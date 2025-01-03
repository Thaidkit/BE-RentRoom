package com.n3c3.rentroom.service;

import com.n3c3.rentroom.dto.PostDTO;
import com.n3c3.rentroom.entity.Category;
import com.n3c3.rentroom.entity.Post;
import com.n3c3.rentroom.entity.User;
import com.n3c3.rentroom.repository.PostRepository;
import com.n3c3.rentroom.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Optional;

@Service
public class PostService {

    private final PostRepository postRepository;
    private UserRepository userRepository;

    @Autowired
    public PostService(PostRepository postRepository) {
        this.postRepository = postRepository;
    }

    // Create a new Post
//    public Post createPost(PostDTO postDTO) {
//        // Lấy user từ userId (giả sử userId được truyền trong UserDTO)
//        User user = userRepository.findById(postDTO.getUser().getId())
//                .orElseThrow(() -> new RuntimeException("User not found"));
//
//        // Lấy category từ categoryId (giả sử categoryId được truyền trong CategoryDTO)
//        Category category = categoryRepository.findById(postDTO.getCategory().getId())
//                .orElseThrow(() -> new RuntimeException("Category not found"));
//
//        // Trừ tiền của user: 100/tuần
//        user.setTotalMoney(user.getTotalMoney() - 100);
//        userRepository.save(user);
//
//        // Tạo đối tượng Post
//        Post post = new Post();
//        post.setTitle(postDTO.getTitle());
//        post.setAddress(postDTO.getAddress());
//        post.setPrice(postDTO.getPrice());
//        post.setRoomSize(postDTO.getRoomSize());
//        post.setDescription(postDTO.getDescription());
//        post.setImages(postDTO.getImages());
//        post.setVideos(postDTO.getVideos());
//        post.setExpiredDate(LocalDate.now().plusDays(7)); // Cộng 7 ngày
//
//        // Thiết lập quan hệ
//        post.setUser(user);
//        post.setCategory(category);
//
//        // Lưu vào cơ sở dữ liệu
//        return postRepository.save(post);
//    }


    // Update a Post
    public Post updatePost(Long id, Post postDetails) {
        Post existingPost = getPostById(id);
        existingPost.setTitle(postDetails.getTitle());
        existingPost.setAddress(postDetails.getAddress());
        existingPost.setPrice(postDetails.getPrice());
        existingPost.setRoomSize(postDetails.getRoomSize());
        existingPost.setDescription(postDetails.getDescription());
        existingPost.setImages(postDetails.getImages());
        existingPost.setVideos(postDetails.getVideos());
        existingPost.setUser(postDetails.getUser());
        existingPost.setCategory(postDetails.getCategory());
        return postRepository.save(existingPost);
    }

    // get post by id
    public Post getPostById(Long id) {
        Optional<Post> post = postRepository.findById(id);
        return post.orElse(null);  // Nếu không tìm thấy bài viết trả về null
    }
    // get all post
    public List<Post> getAllPosts() {
        return postRepository.findAll();
    }
    // Delete a Post
    public void deletePost(Long id) {
        Post existingPost = getPostById(id);
        postRepository.delete(existingPost);
    }
}
