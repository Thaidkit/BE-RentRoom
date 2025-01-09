package com.n3c3.rentroom.service;

import com.n3c3.rentroom.dto.ObjectResponse;
import com.n3c3.rentroom.dto.PostDTO;
import com.n3c3.rentroom.entity.Media;
import com.n3c3.rentroom.entity.Post;
import com.n3c3.rentroom.entity.PostFavorite;
import com.n3c3.rentroom.entity.User;
import com.n3c3.rentroom.repository.PostFavoriteRepository;
import com.n3c3.rentroom.repository.PostRepository;
import com.n3c3.rentroom.repository.UserRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.List;
import java.util.logging.Logger;
import java.util.stream.Collectors;

@Service
public class PostFavoriteService {

    private final Logger log = Logger.getLogger(this.getClass().getName());

    private  final UserRepository userRepository;
    private final PostRepository postRepository;
    private final PostFavoriteRepository postFavoriteRepository;

    public PostFavoriteService(UserRepository userRepository,
                               PostRepository postRepository,
                               PostFavoriteRepository postFavoriteRepository) {
        this.userRepository = userRepository;
        this.postRepository = postRepository;
        this.postFavoriteRepository = postFavoriteRepository;
    }

    @Transactional
    public ResponseEntity<?> saveFavorite(Long userId, Long postId) {
        try {
            User user = userRepository.findById(userId)
                    .orElseThrow(() -> new RuntimeException("User not found"));
            Post post = postRepository.findById(postId)
                    .orElseThrow(() -> new RuntimeException("Post not found"));

            if (postFavoriteRepository.findByUserIdAndPostId(userId, postId).isPresent()) {
                return ResponseEntity.status(HttpStatus.CONFLICT)
                        .body(new ObjectResponse(409, "Post already saved to favorites", null));
            }

            PostFavorite favorite = new PostFavorite();
            favorite.setUser(user);
            favorite.setPost(post);
            postFavoriteRepository.save(favorite);

            return ResponseEntity.ok().body(new ObjectResponse(200, "Post saved to favorites", null));
        }catch (Exception e) {
            log.warning("Error saving favorite post:" + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ObjectResponse(500, "Internal Server Error", null));
        }
    }

    @Transactional
    public ResponseEntity<?> removeFavorite(Long postId) {
        try {
            postFavoriteRepository.deleteByPostId(postId);
            return ResponseEntity.ok().body(new ObjectResponse(200, "Post removed from favorites", null));
        }catch (Exception e) {
            log.warning("Error deleting:" + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ObjectResponse(500, "Internal Server Error", null));
        }

    }

    public ResponseEntity<?> getFavoritesByUser(Long userId) {
        try {
            List<PostFavorite> favoritePosts = postFavoriteRepository.findAllByUserId(userId);

            if (favoritePosts.isEmpty()) {
                return ResponseEntity.status(404).body(new ObjectResponse(404, "No favorite posts found for this user.", ""));
            }

            List<PostDTO> postDTOList = favoritePosts.stream().map(favorite -> {
                Post post = favorite.getPost();
                return new PostDTO(post.getId(),
                        post.getTitle(),
                        post.getAddress(),
                        post.getPrice(),
                        post.getRoomSize(),
                        post.getDescription(),
                        post.getCategory(),
                        post.getContactEmail(),
                        post.getContactPhone(),
                        post.getExpiredDate(),
                        Collections.singletonList(post.getMedia().stream().map(Media::getUrl).findFirst().orElse(null)),
                        post.getCreateAt(),
                        post.getUser().getFullName());
            }).collect(Collectors.toList());
            return ResponseEntity.ok().body(new ObjectResponse(200, "Get all favorite posts", postDTOList));
        }catch (Exception e) {
            log.warning(e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ObjectResponse(500, "Internal Server Error", null));
        }
    }

    public ResponseEntity<?> countUserSavePostForUser(Long userId) {
        try {
            User user = userRepository.findById(userId).orElseThrow(() -> new RuntimeException("User not found"));
            List<Long> idPostList = postRepository.getPostIdListByUserId(user.getId());
            log.info(idPostList.toString());
            Integer countUserSavePost = postFavoriteRepository.countUserFavedPost(idPostList);
            log.info(countUserSavePost.toString());
            return ResponseEntity.ok().body(new ObjectResponse(200, "Count user save post", countUserSavePost));
        } catch (Exception e){
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
