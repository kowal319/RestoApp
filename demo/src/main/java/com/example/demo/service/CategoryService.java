package com.example.demo.service;

import com.example.demo.entity.Category;
import com.example.demo.entity.Product;

import java.util.List;

public interface CategoryService {
    List<Product> getProductsByCategory(Long categoryId);

    Category createCategory(Category category);

    void deleteCategory(Long id);

    List<Category> findAllCategories();

    Category findById(Long id);

    Category updateCategory(Long id, Category updateCategory);
}
