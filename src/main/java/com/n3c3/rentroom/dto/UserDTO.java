package com.n3c3.rentroom.dto;
public class UserDTO {
    private Long id;

    // Constructor, getter và setter
    public UserDTO(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
}
