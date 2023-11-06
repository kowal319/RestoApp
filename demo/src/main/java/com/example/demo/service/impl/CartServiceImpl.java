package com.example.demo.service.impl;

import com.example.demo.Cart;
import com.example.demo.entity.Product;
import com.example.demo.repository.ProductRepository;
import com.example.demo.service.CartService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CartServiceImpl implements CartService {

    private final ProductRepository productRepository;

    private final Cart cart;

    @Autowired
    public CartServiceImpl(ProductRepository productRepository, Cart cart) {
        this.productRepository = productRepository;
        this.cart = cart;
    }
    @Override
    public void addItemToCart(Long productId) {

    }
}
