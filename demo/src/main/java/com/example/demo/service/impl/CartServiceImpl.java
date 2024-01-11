package com.example.demo.service.impl;

import com.example.demo.Cart;
import com.example.demo.entity.Product;
import com.example.demo.repository.ProductRepository;
import com.example.demo.service.CartService;
import com.example.demo.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CartServiceImpl implements CartService {

    private final ProductRepository productRepository;

    private final ProductService productService;

    private final Cart cart;

    @Autowired
    public CartServiceImpl(ProductRepository productRepository, ProductService productService, Cart cart) {
        this.productRepository = productRepository;
        this.productService = productService;
        this.cart = cart;
    }


    @Override
    public void addItemToCart(Long productId) {
        Product product = productService.findById(productId);
        cart.addProduct(product);
    }

    @Override
    public void decreaseItem(Long productId) {
        Product product = productService.findById(productId);
        cart.decreaseProduct(product);
    }

    @Override
    public List<Product> getAllProducts() {
        return productRepository.findAll();
    }

    @Override
    public void removeProduct(Long productId) {
        Product product = productService.findById(productId);
        cart.removeAllProductsFromCart(product);
    }


}

