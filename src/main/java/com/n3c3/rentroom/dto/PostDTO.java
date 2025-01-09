package com.n3c3.rentroom.dto;

import com.n3c3.rentroom.entity.Media;
import com.n3c3.rentroom.entity.Post;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@AllArgsConstructor
public class PostDTO {
    private Long id;
    private String title;
    private String address;
    private Long price;
    private Double roomSize;
    private String description;
    private LocalDate expiredDate;
    private Long userId;
    private String fullName;
    private String phone;
    private String category;
    private String contactPhone;
    private String contactEmail;
    private String link;
    private List<String> mediaUrls;

    public PostDTO() {}

    public PostDTO(Long id, String title, String address, Long price, Double roomSize, String description, String category, String contactEmail, String contactPhone, LocalDate expiredDate, List<String> mediaUrls, LocalDate createAt, String fullName) {
        this.id = id;
        this.title = title;
        this.address = address;
        this.price = price;
        this.roomSize = roomSize;
        this.description = description;
        this.category = category;
        this.contactEmail = contactEmail;
        this.contactPhone = contactPhone;
        this.expiredDate = expiredDate;
        this.mediaUrls = mediaUrls;
        this.fullName = fullName;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public Long getPrice() {
        return price;
    }

    public void setPrice(Long price) {
        this.price = price;
    }

    public Double getRoomSize() {
        return roomSize;
    }

    public void setRoomSize(Double roomSize) {
        this.roomSize = roomSize;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public LocalDate getExpiredDate() {
        return expiredDate;
    }

    public void setExpiredDate(LocalDate expiredDate) {
        this.expiredDate = expiredDate;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getContactPhone() {
        return contactPhone;
    }

    public void setContactPhone(String contactPhone) {
        this.contactPhone = contactPhone;
    }

    public String getContactEmail() {
        return contactEmail;
    }

    public void setContactEmail(String contactEmail) {
        this.contactEmail = contactEmail;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public List<String> getMediaUrls() {
        return mediaUrls;
    }

    public void setMediaUrls(List<String> mediaUrls) {
        this.mediaUrls = mediaUrls;
    }
}

