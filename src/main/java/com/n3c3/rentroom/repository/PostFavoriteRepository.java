package com.n3c3.rentroom.repository;

import com.n3c3.rentroom.entity.PostFavorite;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PostFavoriteRepository extends JpaRepository<PostFavorite, Long> {

    Optional<PostFavorite> findByUserIdAndPostId(Long userId, Long postId);

    @Query(value = "SELECT f.* FROM favorite f INNER JOIN post p ON f.post_id = p.id WHERE f.user_id = :userId", nativeQuery = true)
    List<PostFavorite> findAllByUserId(@Param("userId") Long userId);

    @Modifying
    @Query(value = "delete from favorite where post_id = :id", nativeQuery = true)
    void deleteByPostId(@Param("id") Long id);

    @Query(value = "select count(id) from favorite where post_id in (:idPostList)", nativeQuery = true)
    Integer countUserFavedPost(@Param("idPostList") List<Long> idPostList);
}
