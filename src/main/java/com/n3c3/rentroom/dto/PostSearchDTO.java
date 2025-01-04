package com.n3c3.rentroom.dto;

public class PostSearchDTO {
    private String title;
    private String address;
    private Long minPrice;
    private Long maxPrice;
    private Double minRoomSize;
    private Double maxRoomSize;
    private String category;
    private int page = 0;
    private int size = 10;
    private String sortBy = "createdAt";
    private String sortDirection = "DESC";

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

    public int getPage() {
        return page;
    }

    public void setPage(int page) {
        this.page = page;
    }

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }

    public String getSortBy() {
        return sortBy;
    }

    public void setSortBy(String sortBy) {
        this.sortBy = sortBy;
    }

    public String getSortDirection() {
        return sortDirection;
    }

    public void setSortDirection(String sortDirection) {
        this.sortDirection = sortDirection;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }
}
