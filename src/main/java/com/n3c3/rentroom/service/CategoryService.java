package com.n3c3.rentroom.service;

import com.n3c3.rentroom.dto.CategoryDTO;
import com.n3c3.rentroom.dto.ObjectResponse;
import com.n3c3.rentroom.entity.Category;
import com.n3c3.rentroom.repository.CategoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public class CategoryService {

    @Autowired
    private CategoryRepository categoryRepository;

    // CREATE
    public ResponseEntity<?> createCategory(CategoryDTO categoryDTO) {
        try {
            Category category = new Category();
            category.setCategoryName(categoryDTO.getCategoryName());
            Category savedCategory = categoryRepository.save(category);
            return ResponseEntity.ok(new ObjectResponse(200, "Category created successfully!", savedCategory));
        } catch (Exception e) {
            return ResponseEntity.status(500).body(new ObjectResponse(500, "Error creating category!", e.getMessage()));
        }
    }

    // READ ALL
    public ResponseEntity<?> getAllCategories() {
        try {
            List<Category> categories = categoryRepository.findAll();
            return ResponseEntity.ok(new ObjectResponse(200, "Categories fetched successfully!", categories));
        } catch (Exception e) {
            return ResponseEntity.status(500).body(new ObjectResponse(500, "Error fetching categories!", e.getMessage()));
        }
    }

    // READ BY ID
    public ResponseEntity<?> getCategoryById(Long id) {
        try {
            Category category = categoryRepository.findById(id)
                    .orElseThrow(() -> new RuntimeException("Category not found!"));
            return ResponseEntity.ok(new ObjectResponse(200, "Category fetched successfully!", category));
        } catch (Exception e) {
            return ResponseEntity.status(404).body(new ObjectResponse(404, "Category not found!", e.getMessage()));
        }
    }

    // UPDATE
    public ResponseEntity<?> updateCategory(Long id, CategoryDTO categoryDTO) {
        try {
            Category category = categoryRepository.findById(id)
                    .orElseThrow(() -> new RuntimeException("Category not found!"));

            category.setCategoryName(categoryDTO.getCategoryName());
            category.setModifyAt(LocalDate.now());
            categoryRepository.save(category);

            return ResponseEntity.ok(new ObjectResponse(200, "Category updated successfully!", category));
        } catch (Exception e) {
            return ResponseEntity.status(500).body(new ObjectResponse(500, "Error updating category!", e.getMessage()));
        }
    }

    // DELETE
    public ResponseEntity<?> deleteCategory(Long id) {
        try {
            Category category = categoryRepository.findById(id)
                    .orElseThrow(() -> new RuntimeException("Category not found!"));

            categoryRepository.delete(category);
            return ResponseEntity.ok(new ObjectResponse(200, "Category deleted successfully!", null));
        } catch (Exception e) {
            return ResponseEntity.status(500).body(new ObjectResponse(500, "Error deleting category!", e.getMessage()));
        }
    }
}
