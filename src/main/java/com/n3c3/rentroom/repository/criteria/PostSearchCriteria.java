package com.n3c3.rentroom.repository.criteria;

public class PostSearchCriteria {
    private String title;
    private String address;
    private Long minPrice;
    private Long maxPrice;
    private Double minRoomSize;
    private Double maxRoomSize;
    private String category;

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

    public Long getMinPrice() {
        return minPrice;
    }

    public void setMinPrice(Long minPrice) {
        this.minPrice = minPrice;
    }

    public Long getMaxPrice() {
        return maxPrice;
    }

    public void setMaxPrice(Long maxPrice) {
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

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }
}
