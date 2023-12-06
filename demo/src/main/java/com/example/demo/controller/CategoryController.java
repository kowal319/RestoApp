package com.example.demo.controller;


import com.example.demo.entity.Category;
import com.example.demo.service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/category")
public class CategoryController {

    private final CategoryService categoryService;

    @Autowired
    public CategoryController(CategoryService categoryService) {
        this.categoryService = categoryService;
    }

    @GetMapping
    public String viewCategories(Model model){
        List<Category> categories = categoryService.findAllCategories();
        model.addAttribute("categories", categories);
        return "admin/product/categories";
    }

    @PostMapping("/save")
    public String createCategory(@ModelAttribute("category") Category category){
        categoryService.createCategory(category);
        return "redirect:/category";
    }

    @GetMapping("/newCategory")
    public String addNewCategoryForm(Model model){
        model.addAttribute("categories", new Category());
        return "admin/product/newCategory";
    }

    @GetMapping("/{id}")
    public ResponseEntity<Category> getCategoryById(@PathVariable Long id){
        Category category = categoryService.findById(id);
        if (category != null){
            return ResponseEntity.ok(category);
        }else {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/deleteCategory/{id}")
    public String deleteCategory(@PathVariable("id") Long id){
        categoryService.deleteCategory(id);
        return "redirect:/category";
    }
    @GetMapping("/editCategory/{id}")
    public String editCategoryForm(@PathVariable Long id, Model model){
        Category category= categoryService.findById(id);
        model.addAttribute("category", category);
        return "admin/product/editCategory";
    }
    @PostMapping("/editCategory/{id}")
    public String editCategory(@PathVariable Long id, @ModelAttribute Category updatedCategory){
        categoryService.updateCategory(id, updatedCategory);
        return "redirect:/category";
    }
}
