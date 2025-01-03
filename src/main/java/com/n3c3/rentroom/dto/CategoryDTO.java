package com.n3c3.rentroom.dto;
public class CategoryDTO {
    private Long id;

    // Constructor, getter và setter
    public CategoryDTO(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
}
