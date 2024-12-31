package com.n3c3.rentroom.entity;

import jakarta.persistence.Column;
import jakarta.persistence.MappedSuperclass;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;

import java.time.LocalDate;

@MappedSuperclass
@Getter
@Setter
public abstract class AbstractAudittingEntity {

    @CreatedDate
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDate createAt;

    @LastModifiedDate
    @Column(name = "modified_at", nullable = false, updatable = false)
    private LocalDate modifyAt;
}
