package com.n3c3.rentroom.repository.specification;

import com.n3c3.rentroom.entity.Post;
import com.n3c3.rentroom.repository.criteria.PostSearchCriteria;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Root;
import jakarta.persistence.criteria.Predicate;
import org.springframework.data.jpa.domain.Specification;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class PostSpecification implements Specification<Post> {

    private final PostSearchCriteria criteria;

    public PostSpecification(PostSearchCriteria criteria) {
        this.criteria = criteria;
    }

    @Override
    public Predicate toPredicate(Root<Post> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) {
        List<Predicate> predicates = new ArrayList<>();

        if (criteria.getTitle() != null) {
            predicates.add(criteriaBuilder.like(root.get("title"), "%" + criteria.getTitle() + "%"));
        }
        if (criteria.getAddress() != null) {
            predicates.add(criteriaBuilder.like(root.get("address"), "%" + criteria.getAddress() + "%"));
        }
        if (criteria.getMinPrice() != null) {
            predicates.add(criteriaBuilder.greaterThanOrEqualTo(root.get("price"), criteria.getMinPrice()));
        }
        if (criteria.getMaxPrice() != null) {
            predicates.add(criteriaBuilder.lessThanOrEqualTo(root.get("price"), criteria.getMaxPrice()));
        }
        if (criteria.getMinRoomSize() != null) {
            predicates.add(criteriaBuilder.greaterThanOrEqualTo(root.get("roomSize"), criteria.getMinRoomSize()));
        }
        if (criteria.getMaxRoomSize() != null) {
            predicates.add(criteriaBuilder.lessThanOrEqualTo(root.get("roomSize"), criteria.getMaxRoomSize()));
        }
        if (criteria.getFullName() != null) {
            predicates.add(criteriaBuilder.like(root.get("user").get("fullName"), "%" + criteria.getFullName() + "%"));
        }
        if (criteria.getPhone() != null) {
            predicates.add(criteriaBuilder.like(root.get("user").get("phone"), "%" + criteria.getPhone() + "%"));
        }

        predicates.add(criteriaBuilder.greaterThanOrEqualTo(root.get("expiredDate"), LocalDate.now()));

        return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
    }
}

