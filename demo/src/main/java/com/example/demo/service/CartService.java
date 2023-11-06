package com.example.demo.service;

import com.example.demo.entity.Product;

import java.util.List;

public interface CartService {
    void addItemToCart(Long productId);

    void decreaseItem(Long productId);

    List<Product> getAllProducts();
}
