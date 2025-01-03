package com.n3c3.rentroom.repository.criteria;

public class PostSearchCriteria {
    private String title;
    private String address;
    private Double minPrice;
    private Double maxPrice;
    private Double minRoomSize;
    private Double maxRoomSize;
    private String fullName;
    private String phone;

    // Getters and Setters
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

    public Double getMinPrice() {
        return minPrice;
    }

    public void setMinPrice(Double minPrice) {
        this.minPrice = minPrice;
    }

    public Double getMaxPrice() {
        return maxPrice;
    }

    public void setMaxPrice(Double maxPrice) {
        this.maxPrice = maxPrice;
    }

    public Double getMinRoomSize() {
        return minRoomSize;
    }

    public void setMinRoomSize(Double minRoomSize) {
        this.minRoomSize = minRoomSize;
    }

    public Double getMaxRoomSize() {
        return maxRoomSize;
    }

    public void setMaxRoomSize(Double maxRoomSize) {
        this.maxRoomSize = maxRoomSize;
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
}
