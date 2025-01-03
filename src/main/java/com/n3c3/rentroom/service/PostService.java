package com.n3c3.rentroom.service;

import com.n3c3.rentroom.dto.ObjectResponse;
import com.n3c3.rentroom.dto.PostDTO;
import com.n3c3.rentroom.dto.PostSearchDTO;
import com.n3c3.rentroom.entity.Category;
import com.n3c3.rentroom.entity.Media;
import com.n3c3.rentroom.entity.Post;
import com.n3c3.rentroom.entity.User;
import com.n3c3.rentroom.repository.CategoryRepository;
import com.n3c3.rentroom.repository.MediaRepository;
import com.n3c3.rentroom.repository.PostRepository;
import com.n3c3.rentroom.repository.UserRepository;
import com.n3c3.rentroom.repository.criteria.PostSearchCriteria;
import com.n3c3.rentroom.repository.specification.PostSpecification;
import lombok.extern.log4j.Log4j2;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Optional;
import java.util.logging.Logger;
import java.util.stream.Collectors;

@Service
@Transactional
public class PostService {

    private final Logger log = Logger.getLogger(PostService.class.getName());

    private final PostRepository postRepository;
    private final UserRepository userRepository;
    private final CategoryRepository categoryRepository;
    private final MediaRepository mediaRepository;

    public PostService(PostRepository postRepository,
                       UserRepository userRepository,
                       CategoryRepository categoryRepository,
                       MediaRepository mediaRepository) {
        this.postRepository = postRepository;
        this.userRepository = userRepository;
        this.categoryRepository = categoryRepository;
        this.mediaRepository = mediaRepository;
    }

    public ResponseEntity<?> createPost(PostDTO postDTO) {
        try {
            Post post = new Post();
            post.setTitle(postDTO.getTitle());
            post.setAddress(postDTO.getAddress());
            post.setPrice(postDTO.getPrice());
            post.setRoomSize(postDTO.getRoomSize());
            post.setDescription(postDTO.getDescription());
            post.setExpiredDate(postDTO.getExpiredDate());

            // Gắn User và Category
            User user = userRepository.findById(postDTO.getUserId())
                    .orElseThrow(() -> new RuntimeException("User not found"));
            post.setUser(user);

            Category category = categoryRepository.findById(postDTO.getCategoryId())
                    .orElseThrow(() -> new RuntimeException("Category not found"));
            post.setCategory(category);

            postRepository.save(post);

            // Lưu Media
            for (String mediaUrl : postDTO.getMediaUrls()) {
                Media media = new Media();
                media.setUrl(mediaUrl);
                media.setPost(post);
                mediaRepository.save(media);
            }

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

    public ResponseEntity<ObjectResponse> getPostById(Long id) {
        try {

                Post post = postRepository.findById(id)
                        .orElseThrow(() -> new RuntimeException("Post with id " + id + " not found"));

            return ResponseEntity.ok().body(new ObjectResponse(200, "Fetched post successfully!", post));
        } catch (Exception e) {
            return ResponseEntity.status(500).body(new ObjectResponse(500, "Error fetching post!", e.getMessage()));
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
            searchCriteria.setFullName(searchDTO.getFullName());
            searchCriteria.setPhone(searchDTO.getPhone());

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
                        dto.setCategoryId(post.getCategory().getId());

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
}
