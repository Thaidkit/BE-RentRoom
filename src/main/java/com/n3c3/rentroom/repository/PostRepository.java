package com.n3c3.rentroom.repository;

import com.n3c3.rentroom.entity.Post;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PostRepository extends JpaRepository<Post, Long> {
    // Tìm bài viết theo id
    Post findById(long id);
}
