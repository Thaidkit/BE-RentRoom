package com.n3c3.rentroom.repository;

import com.n3c3.rentroom.entity.Post;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PostRepository extends JpaRepository<Post, Long>, JpaSpecificationExecutor<Post> {
    // Tìm bài viết theo id
    Post findById(long id);

    @Query(value = "select p.id from post p inner join user u on p.user_id = u.id where p.user_id = :userId", nativeQuery = true)
    List<Long> getPostIdListByUserId(@Param("userId") Long userId);
}
