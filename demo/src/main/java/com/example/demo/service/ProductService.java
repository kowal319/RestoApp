package com.example.demo.service;

import com.example.demo.entity.Product;

import java.util.List;

public interface ProductService {

    List<Product> findAllProducts();
    Product findById(Long id);
    Product createProduct(Product product);
    void deleteProduct(Long id);
    Product updateProduct(Long id, Product updateProduct);
}
