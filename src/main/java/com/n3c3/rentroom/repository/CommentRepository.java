package com.n3c3.rentroom.repository;

import com.n3c3.rentroom.entity.Comment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CommentRepository extends JpaRepository<Comment, Long> {

    @Modifying
    @Query(value = "INSERT INTO comment (user_id, post_id, comment, created_at, modified_at) " +
            "VALUES (:userId, :postId, :comment, CURRENT_DATE, CURRENT_DATE)",
            nativeQuery = true)
    void saveComment(@Param("userId") Long userId,
                     @Param("postId") Long postId,
                     @Param("comment") String comment);

    @Query(value = "select * from comment where post_id = :postId", nativeQuery = true)
    List<Comment> getListCommentByPostId(@Param("postId") Long postId);
}
