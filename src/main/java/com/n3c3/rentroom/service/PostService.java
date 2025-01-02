package com.n3c3.rentroom.service;

import com.n3c3.rentroom.entity.Post;
import com.n3c3.rentroom.repository.PostRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class PostService {

    private final PostRepository postRepository;

    @Autowired
    public PostService(PostRepository postRepository) {
        this.postRepository = postRepository;
    }

    // Lấy bài viết theo id
    public Post getPostById(Long id) {
        Optional<Post> post = postRepository.findById(id);
        return post.orElse(null);  // Nếu không tìm thấy bài viết trả về null
    }
}
