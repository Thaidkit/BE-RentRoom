package com.n3c3.rentroom.dto;

import com.n3c3.rentroom.dto.CategoryDTO;
import com.n3c3.rentroom.dto.UserDTO;
public class PostDTO {
    private Long id;
    private String title;
    private String address;
    private Double price;
    private Double roomSize;
    private String description;
    private String images;
    private String videos;
    private String expiredDate;
    private UserDTO user;
    private CategoryDTO category;

    // Constructor
    public PostDTO(Long id, String title, String address, Double price, Double roomSize, String description,
                   String images, String videos, String expiredDate, UserDTO user, CategoryDTO category) {
        this.id = id;
        this.title = title;
        this.address = address;
        this.price = price;
        this.roomSize = roomSize;
        this.description = description;
        this.images = images;
        this.videos = videos;
        this.expiredDate = expiredDate;
        this.user = user;
        this.category = category;
    }

    // Getter và Setter cho id
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    // Getter và Setter cho title
    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    // Getter và Setter cho address
    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    // Getter và Setter cho price
    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    // Getter và Setter cho roomSize
    public Double getRoomSize() {
        return roomSize;
    }

    public void setRoomSize(Double roomSize) {
        this.roomSize = roomSize;
    }

    // Getter và Setter cho description
    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    // Getter và Setter cho images
    public String getImages() {
        return images;
    }

    public void setImages(String images) {
        this.images = images;
    }

    // Getter và Setter cho videos
    public String getVideos() {
        return videos;
    }

    public void setVideos(String videos) {
        this.videos = videos;
    }

    // Getter và Setter cho expiredDate
    public String getExpiredDate() {
        return expiredDate;
    }

    public void setExpiredDate(String expiredDate) {
        this.expiredDate = expiredDate;
    }

    // Getter và Setter cho user
    public UserDTO getUser() {
        return user;
    }

    public void setUser(UserDTO user) {
        this.user = user;
    }

    // Getter và Setter cho category
    public CategoryDTO getCategory() {
        return category;
    }

    public void setCategory(CategoryDTO category) {
        this.category = category;
    }
}
