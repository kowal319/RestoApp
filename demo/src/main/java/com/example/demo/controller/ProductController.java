package com.example.demo.controller;

import com.example.demo.Cart;
import com.example.demo.entity.Product;
import com.example.demo.service.CartService;
import com.example.demo.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/products")
public class ProductController {

    private final ProductService productService;
    private final Cart cart;

    private final CartService cartService;


    @Autowired
    public ProductController(ProductService productService, Cart cart, CartService cartService) {
        this.productService = productService;
        this.cart = cart;
        this.cartService = cartService;
    }

    @GetMapping()
    public String viewAllProducts(Model model) {
        List<Product> products = productService.findAllProducts();
        model.addAttribute("products", products);
        return "product/products";
    }

    @GetMapping("/admin/allProducts")
    public String viewAllProductsAdmin(Model model) {
        List<Product> products = productService.findAllProducts();
        model.addAttribute("products", products);
        return "admin/productsAdmin";
    }

    @PostMapping("/save")
    public String createProduct(@ModelAttribute("product") Product product) {
         productService.createProduct(product);
         return "redirect:/products";
    }

    @GetMapping("/newProduct")
    public String showAddProductPage(Model model) {
        model.addAttribute("product", new Product());
        return "product/add-product";
    }

    @GetMapping("/{id}")
    public ResponseEntity<Product> getProductById(@PathVariable Long id) {
        Product product = productService.findById(id);
        if (product != null) {
            return ResponseEntity.ok(product);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/deleteProduct/{id}")
    public String deleteProduct(@PathVariable("id") Long id){
        productService.deleteProduct(id);
        return "redirect:/products";
}



    @GetMapping("/editProduct/{id}")
    public String editProductForm(@PathVariable Long id, Model model) {
        Product product = productService.findById(id);
        model.addAttribute("product", product);
        return "product/edit-product"; // Create an "edit-product.html" template
    }

    @PostMapping("/editProduct/{id}")
    public String editProduct(@PathVariable Long id, @ModelAttribute Product updatedProduct) {
        productService.updateProduct(id, updatedProduct);
        return "redirect:/products"; // Redirect to the product list page after editing.
    }

    @GetMapping("/addProduct/{productId}")
    public String addItemToCart(@PathVariable("productId") Long productId, Model model){
        cartService.addItemToCart(productId);
        model.addAttribute("products", cartService.getAllProducts());
        return "product/products";
    }


//    @GetMapping("/add/{productId}")
//    public String addItemToCart(@PathVariable("productId") Long itemId, Model model){
//        Optional<Product> oProduct = productService.findById(itemId);
//        if(oProduct.isPresent()){
//            Product product = oProduct.get();
//            cart.addProduct(product);
//        }
//        model.addAttribute("products", productService.findAllProducts());
//        return "products";
//    }
}