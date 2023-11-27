package com.example.demo.service;

import com.example.demo.entity.Product;

import java.util.List;

public interface CategoryService {
    List<Product> getProductsByCategory(Long categoryId);
}
