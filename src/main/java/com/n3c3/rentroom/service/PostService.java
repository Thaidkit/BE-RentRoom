package com.n3c3.rentroom.service;

import com.n3c3.rentroom.dto.*;
import com.n3c3.rentroom.entity.Media;
import com.n3c3.rentroom.entity.Post;
import com.n3c3.rentroom.entity.User;
import com.n3c3.rentroom.repository.MediaRepository;
import com.n3c3.rentroom.repository.PostRepository;
import com.n3c3.rentroom.repository.UserRepository;
import com.n3c3.rentroom.repository.criteria.PostSearchCriteria;
import com.n3c3.rentroom.repository.specification.PostSpecification;
import jakarta.persistence.criteria.Predicate;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;
import java.util.stream.Collectors;

@Service
@Transactional
public class PostService {

    private final Logger log = Logger.getLogger(PostService.class.getName());

    private final PostRepository postRepository;
    private final UserRepository userRepository;
    private final MediaRepository mediaRepository;

    public PostService(PostRepository postRepository,
                       UserRepository userRepository,
                       MediaRepository mediaRepository) {
        this.postRepository = postRepository;
        this.userRepository = userRepository;
        this.mediaRepository = mediaRepository;
    }

    public ResponseEntity<?> createPost(PostCreateDTO postDTO) {
        try {

            User user = userRepository.findById(postDTO.getUserId())
                    .orElseThrow(() -> new RuntimeException("User not found"));

            Long totalMoney = user.getTotalMoney();
            if (totalMoney < postDTO.getFeeToPost()) {
                return ResponseEntity.status(500).body(new ObjectResponse(500, "You dont have enough money to post!", ""));
            }
            user.setTotalMoney(totalMoney - postDTO.getFeeToPost());
            userRepository.save(user);

            Post post = new Post();
            post.setTitle(postDTO.getTitle());
            post.setAddress(postDTO.getAddress());
            post.setPrice(postDTO.getPrice());
            post.setRoomSize(postDTO.getRoomSize());
            post.setDescription(postDTO.getDescription());
            post.setLink(postDTO.getLink());
            post.setCategory(postDTO.getCategory());
            post.setContactPhone(postDTO.getContactPhone());
            post.setContactEmail(postDTO.getContactEmail());
            post.setExpiredDate(LocalDate.now().plusDays(postDTO.getAmountExpiredDays()));
            post.setCreateAt(LocalDate.now());
            post.setModifyAt(LocalDate.now());

            post.setUser(user);

            List<Media> mediaList = postDTO.getMediaUrls().stream()
                    .map(url -> {
                        Media media = new Media();
                        media.setUrl(url);
                        media.setPost(post); // Liên kết với Post
                        return media;
                    })
                    .collect(Collectors.toList());
            post.setMedia(mediaList);

            postRepository.save(post);

            return ResponseEntity.ok(new ObjectResponse(200, "Created post successfully!", post));
        } catch (Exception e) {
            return ResponseEntity.status(500).body(new ObjectResponse(500, "Error creating post!", e.getMessage()));
        }
    }

    // Update a Post
    public ResponseEntity<?> updatePost(Long id, PostDTO postDTO) {
        try {
            Post post = postRepository.findById(id)
                    .orElseThrow(() -> new RuntimeException("Post not found"));

            post.setTitle(postDTO.getTitle());
            post.setAddress(postDTO.getAddress());
            post.setPrice(postDTO.getPrice());
            post.setRoomSize(postDTO.getRoomSize());
            post.setDescription(postDTO.getDescription());
            post.setExpiredDate(postDTO.getExpiredDate());
            post.setModifyAt(LocalDate.now());
            post.setContactPhone(postDTO.getContactPhone());
            post.setContactEmail(postDTO.getContactEmail());
            post.setCategory(postDTO.getCategory());
            post.setLink(postDTO.getLink());

            List<Media> mediaOldOfPost = mediaRepository.findAllByPostId(id);
            mediaRepository.deleteAll(mediaOldOfPost);
            for (String mediaUrl : postDTO.getMediaUrls()) {
                Media media = new Media();
                media.setUrl(mediaUrl);
                media.setPost(post);
                mediaRepository.save(media);
            }

            postRepository.save(post);
            return ResponseEntity.ok(new ObjectResponse(200, "Updated post successfully!", post));
        } catch (Exception e) {
            return ResponseEntity.status(500).body(new ObjectResponse(500, "Error updating post!", e.getMessage()));
        }
    }

    @Transactional(propagation = Propagation.NEVER)
    public ResponseEntity<ObjectResponse> getPostById(Long id) {
        try {

                Post post = postRepository.findById(id)
                        .orElseThrow(() -> new RuntimeException("Post with id " + id + " not found"));

            return ResponseEntity.ok().body(new ObjectResponse(200, "Fetched post successfully!", post));
        } catch (Exception e) {
            return ResponseEntity.status(500).body(new ObjectResponse(500, "Error fetching post!", e.getMessage()));
        }
    }

    @Transactional(propagation = Propagation.NEVER)
    public ResponseEntity<?> getALLPostByUserId(Long userId, int pageNumber, int size) {
        try {
          if (!userRepository.findById(userId).isPresent()) {
              return ResponseEntity.status(404).body(new ObjectResponse(404, "User not found", ""));
          }

            // Tạo Pageable để phân trang
            Pageable pageable = PageRequest.of(pageNumber, size, Sort.by("createAt").descending());

            // Tạo Specification để lọc bài viết có expiredDate >= ngày hiện tại
            Specification<Post> spec = (root, query, criteriaBuilder) -> {
                Predicate byUserId = criteriaBuilder.equal(root.get("user").get("id"), userId);
                return criteriaBuilder.and(byUserId);
            };

            Page<Post> postsOfUser = postRepository.findAll(spec, pageable);

            log.info("23232323");
            return ResponseEntity.ok(new ObjectResponse(200, "Posts fetched successfully!", postsOfUser));
        } catch (Exception e) {
            log.info("Error: " + e.getMessage());
            return ResponseEntity.status(500).body(new ObjectResponse(500, "Error fetching posts!", e.getMessage()));
        }
    }

    public ResponseEntity<ObjectResponse> deletePost(Long id) {
        try {
            Post post = postRepository.findById(id)
                    .orElseThrow(() -> new RuntimeException("Post not found with ID: " + id));

            postRepository.delete(post);

            return ResponseEntity.ok().body(new ObjectResponse(200, "Deleted post successfully!", null));
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
            return ResponseEntity.status(500).body(new ObjectResponse(500, "Error deleting post!", e.getMessage()));
        }
    }

    @Transactional(propagation = Propagation.NEVER)
    public ResponseEntity<?> getAllPosts(int pageNumber, int size) {
        try {
            // Kiểm tra các tham số pageNumber và size hợp lệ
            if (pageNumber < 0 || size <= 0) {
                return ResponseEntity.badRequest().body(new ObjectResponse(400, "Invalid page number or size", ""));
            }

            // Tạo Pageable để phân trang
            Pageable pageable = PageRequest.of(pageNumber, size, Sort.by("createAt").descending());

            // Tạo Specification để lọc bài viết có expiredDate >= ngày hiện tại
            Specification<Post> spec = (root, query, criteriaBuilder) -> {
                return criteriaBuilder.greaterThanOrEqualTo(root.get("expiredDate"), LocalDate.now());
            };

            Page<Post> posts = postRepository.findAll(spec, pageable);

            log.info("23232323");
            return ResponseEntity.ok(new ObjectResponse(200, "Posts fetched successfully!", posts));
        } catch (Exception e) {
            // Xử lý lỗi chi tiết
            log.info("Error: " + e.getMessage());
            return ResponseEntity.status(500).body(new ObjectResponse(500, "Error fetching posts!", e.getMessage()));
        }
    }

    @Transactional(propagation = Propagation.NEVER)
    public ResponseEntity<?> searchPostWithMultiConditions(PostSearchDTO searchDTO) {
        try {
            // Tạo SearchCriteria từ các tham số tìm kiếm trong searchDTO
            PostSearchCriteria searchCriteria = new PostSearchCriteria();
            searchCriteria.setTitle(searchDTO.getTitle());
            searchCriteria.setAddress(searchDTO.getAddress());
            searchCriteria.setMinPrice(searchDTO.getMinPrice());
            searchCriteria.setMaxPrice(searchDTO.getMaxPrice());
            searchCriteria.setMinRoomSize(searchDTO.getMinRoomSize());
            searchCriteria.setMaxRoomSize(searchDTO.getMaxRoomSize());
            searchCriteria.setCategory(searchDTO.getCategory());

            // Tạo Specification từ SearchCriteria
            PostSpecification spec = new PostSpecification(searchCriteria);

            // Tạo Sort từ tham số sortBy và sortDirection (Mặc định sắp xếp theo createdAt DESC)
            Sort sort = Sort.by(Sort.by("createAt").descending().toList()); // Mặc định sắp xếp theo createdAt DESC
            if (searchDTO.getSortBy() != null) {
                if ("DESC".equalsIgnoreCase(searchDTO.getSortDirection())) {
                    sort = Sort.by(Sort.by("createAt").descending().toList());
                } else {
                    sort = Sort.by(Sort.by("createAt").descending().toList());
                }
            }

            // Phân trang
            Pageable pageable = PageRequest.of(searchDTO.getPage(), searchDTO.getSize(), sort);

            // Lọc bài viết chưa hết hạn (expiredDate >= hiện tại)
            Specification<Post> expiredFilterSpec = (root, query, criteriaBuilder) -> {
                return criteriaBuilder.greaterThanOrEqualTo(root.get("expiredDate"), LocalDate.now());
            };

            // Kết hợp các Specification (searchCriteria + expired filter)
            Specification<Post> finalSpec = spec.and(expiredFilterSpec);

            // Lấy danh sách bài viết theo Specification và phân trang
            Page<Post> posts = postRepository.findAll(finalSpec, pageable);

            // Trả về thông tin các bài viết và chủ bài viết
            List<PostDTO> postDTOs = posts.stream()
                    .map(post -> {
                        PostDTO dto = new PostDTO();
                        dto.setId(post.getId());
                        dto.setTitle(post.getTitle());
                        dto.setAddress(post.getAddress());
                        dto.setPrice(post.getPrice());
                        dto.setRoomSize(post.getRoomSize());
                        dto.setDescription(post.getDescription());
                        dto.setCategory(post.getCategory());
                        dto.setContactEmail(post.getContactEmail());
                        dto.setContactPhone(post.getContactPhone());
                        dto.setLink(post.getLink());
                        dto.setExpiredDate(post.getExpiredDate());
                        dto.setUserId(post.getUser().getId());
                        dto.setFullName(post.getUser().getFullName());
                        dto.setPhone(post.getUser().getPhone());

                        // Lấy danh sách ảnh của bài đăng
                        List<String> images = mediaRepository.findAllByPostId(post.getId()).stream()
                                .map(Media::getUrl)
                                .collect(Collectors.toList());
                        dto.setMediaUrls(images);

                        return dto;
                    })
                    .collect(Collectors.toList());

            return ResponseEntity.ok().body(new ObjectResponse(200, "Posts fetched successfully!", postDTOs));
        } catch (Exception e) {
            return ResponseEntity.status(500).body(new ObjectResponse(500, "Error fetching posts!", e.getMessage()));
        }
    }

    @Transactional(propagation = Propagation.NEVER)
    public ResponseEntity<?> searchPostByKeyword(String keyword, int pageNumber, int size) {
        try {
            Pageable pageable = PageRequest.of(pageNumber, size, Sort.by("createAt").descending());

            Specification<Post> spec = (root, query, criteriaBuilder) ->
                    criteriaBuilder.or(
                            criteriaBuilder.like(root.get("title"), "%" + keyword + "%"),
                            criteriaBuilder.like(root.get("description"), "%" + keyword + "%"),
                            criteriaBuilder.like(root.get("address"), "%" + keyword + "%"),
                            criteriaBuilder.like(root.get("category"), "%" + keyword + "%")
                    );

            Specification<Post> expiredFilterSpec = (root, query, criteriaBuilder) -> {
                return criteriaBuilder.greaterThanOrEqualTo(root.get("expiredDate"), LocalDate.now());
            };

            // Kết hợp các Specification (searchCriteria + expired filter)
            Specification<Post> finalSpec = spec.and(expiredFilterSpec);

            Page<Post> posts = postRepository.findAll(finalSpec, pageable);

            return ResponseEntity.ok(new ObjectResponse(200, "Posts fetched successfully!", posts));
        } catch (Exception e) {
            return ResponseEntity.status(500).body(new ObjectResponse(500, "Error fetching posts!", e.getMessage()));
        }
    }

    @Transactional(propagation = Propagation.NEVER)
    public ResponseEntity<?> getAllAddress(String keyword) {
        try {
            Specification<Post> spec = (root, query, criteriaBuilder) ->
                    criteriaBuilder.or(
                            criteriaBuilder.like(root.get("title"), "%" + keyword + "%"),
                            criteriaBuilder.like(root.get("description"), "%" + keyword + "%"),
                            criteriaBuilder.like(root.get("address"), "%" + keyword + "%"),
                            criteriaBuilder.like(root.get("category"), "%" + keyword + "%")
                    );

            Specification<Post> expiredFilterSpec = (root, query, criteriaBuilder) -> {
                return criteriaBuilder.greaterThanOrEqualTo(root.get("expiredDate"), LocalDate.now());
            };

            Specification<Post> finalSpec = spec.and(expiredFilterSpec);

            List<Post> posts = postRepository.findAll(finalSpec);
            List<AddressPostSearchMapDTO> addressPostSearchMapDTOList = new ArrayList<>();
            posts.stream().forEach(post -> {
                AddressPostSearchMapDTO addressPostSearchMapDTO = new AddressPostSearchMapDTO();
                addressPostSearchMapDTO.setId(post.getId());
                addressPostSearchMapDTO.setTitle(post.getTitle());
                addressPostSearchMapDTO.setAddress(post.getAddress());
                String imgUrl = post.getMedia().stream()
                        .findFirst()
                        .map(Media::getUrl)
                        .orElse(null);
                addressPostSearchMapDTO.setImgUrl(imgUrl);                
                addressPostSearchMapDTOList.add(addressPostSearchMapDTO);
            });

            return ResponseEntity.ok(new ObjectResponse(200, "Posts fetched successfully!", addressPostSearchMapDTOList));
        } catch (Exception e) {
            return ResponseEntity.status(500).body(new ObjectResponse(500, "Error fetching posts!", e.getMessage()));
        }
    }

    public ResponseEntity<?> prolongPost(Long postId, PostProlongDTO postProlongDTO) {
        try {
            Post post = postRepository.findById(postId).orElseThrow(() -> new RuntimeException("Post not found"));
            User user = userRepository.findById(post.getUser().getId()).orElseThrow(() -> new RuntimeException("User not found"));

            if (LocalDate.now().isBefore(post.getExpiredDate())) {
                if (user.getTotalMoney() < postProlongDTO.getAmountMoneyPayment())
                    return ResponseEntity.badRequest().body(new ObjectResponse(400, "User haven't enough money to prolong this post", ""));
                else {
                    post.setExpiredDate(post.getExpiredDate().plusDays(postProlongDTO.getAmountDaysProlong()));
                    user.setTotalMoney(user.getTotalMoney() - postProlongDTO.getAmountMoneyPayment());
                    userRepository.save(user);
                    postRepository.save(post);
                    return ResponseEntity.ok(new ObjectResponse(200, "Post prolonged successfully!", "Expired date: " + post.getExpiredDate()));
                }
            } else {
                if (user.getTotalMoney() < postProlongDTO.getAmountMoneyPayment())
                    return ResponseEntity.badRequest().body(new ObjectResponse(400, "User haven't enough money to prolong this post", ""));
                else {
                    post.setExpiredDate(LocalDate.now().plusDays(postProlongDTO.getAmountDaysProlong()));
                    user.setTotalMoney(user.getTotalMoney() - postProlongDTO.getAmountMoneyPayment());
                    userRepository.save(user);
                    postRepository.save(post);
                    return ResponseEntity.ok(new ObjectResponse(200, "Post prolonged successfully!", "Expired date: " + post.getExpiredDate()));
                }
            }
        } catch (Exception e) {
            log.warning(e.getMessage());
            return ResponseEntity.badRequest().body(new ObjectResponse(400, "Error prolonging posts!", e.getMessage()));
        }
    }
}
