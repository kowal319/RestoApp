package com.example.demo.service.impl;


import com.example.demo.entity.Category;
import com.example.demo.entity.Product;
import com.example.demo.repository.CategoryRepository;
import com.example.demo.repository.ProductRepository;
import com.example.demo.service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CategoryServiceImpl implements CategoryService {

    @Autowired
    public CategoryRepository categoryRepository;
    @Autowired
    public ProductRepository productRepository;

@Override
    public List<Product> getProductsByCategory(Long categoryId) {
        Category category = categoryRepository.findById(categoryId)
                .orElseThrow(() -> new RuntimeException("Category not found"));

        return productRepository.findByCategory(category);
    }

    @Override
    public Category createCategory(Category category){
    return categoryRepository.save(category);
    }

    @Override
    public void deleteCategory(Long id){
    categoryRepository.deleteById(id);
    }

    @Override
    public List<Category> findAllCategories(){
    return categoryRepository.findAll();
    }

    @Override
    public Category findById(Long id){
    Optional<Category> categoryOptional = categoryRepository.findById(id);
    return categoryOptional.orElse(null);
    }

    @Override
    public Category updateCategory(Long id, Category updateCategory){
    Optional<Category> optionalCategory = categoryRepository.findById(id);
    if(optionalCategory.isPresent()){
        Category existingCategory = optionalCategory.get();
        existingCategory.setName(updateCategory.getName());
        return categoryRepository.save(existingCategory);
    } else {
        return null;
    }
    }


}
