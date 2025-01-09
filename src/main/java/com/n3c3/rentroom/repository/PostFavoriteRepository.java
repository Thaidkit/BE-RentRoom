package com.n3c3.rentroom.repository;

import com.n3c3.rentroom.entity.PostFavorite;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PostFavoriteRepository extends JpaRepository<PostFavorite, Long> {

    Optional<PostFavorite> findByUserIdAndPostId(Long userId, Long postId);

    @Query(value = "SELECT f.* FROM Favorite f INNER JOIN Post p ON f.post_id = p.id WHERE f.user_id = :userId", nativeQuery = true)
    List<PostFavorite> findAllByUserId(@Param("userId") Long userId);

    void deleteById(Long id);
}
