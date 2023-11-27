package com.example.demo.service;

import com.example.demo.entity.Category;
import com.example.demo.entity.Product;

import java.util.List;

public interface ProductService {

    List<Product> findAllProducts();
    Product findById(Long id);
    Product createProduct(Product product);
    void deleteProduct(Long id);
    Product updateProduct(Long id, Product updateProduct);
    String getProductNameById(Long id);

    double getProductPriceById(Long itemId);

    List<Category> getAllCategories();

    List<Product> getProductsByCategory(Long categoryId);
}
