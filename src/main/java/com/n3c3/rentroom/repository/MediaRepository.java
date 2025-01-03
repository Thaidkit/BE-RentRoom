package com.n3c3.rentroom.repository;

import com.n3c3.rentroom.entity.Media;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MediaRepository extends JpaRepository<Media, Long> {

    @Query(value = "SELECT m FROM Media m where m.post.id = :postId")
    List<Media> findAllByPostId(@Param("postId") Long postId);
}
