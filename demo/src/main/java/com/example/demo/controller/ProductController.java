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


    //userview
    @GetMapping()
    public String viewAllProducts(Model model) {
        List<Product> products = productService.findAllProducts();
        model.addAttribute("products", products);
        return "user/order/products";
    }

    @GetMapping("/admin/allProducts")
    public String viewAllProductsAdmin(Model model) {
        List<Product> products = productService.findAllProducts();
        model.addAttribute("products", products);
        return "admin/product/allProducts";
    }

    @PostMapping("/save")
    public String createProduct(@ModelAttribute("product") Product product) {
         productService.createProduct(product);
         return "redirect:/products/admin/allProducts";
    }

    @GetMapping("/newProduct")
    public String showAddProductPage(Model model) {
        model.addAttribute("product", new Product());
        return "admin/product/add-product";
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
        return "redirect:/products/admin/allProducts";
}



    @GetMapping("/editProduct/{id}")
    public String editProductForm(@PathVariable Long id, Model model) {
        Product product = productService.findById(id);
        model.addAttribute("product", product);
        return "admin/product/edit-product"; // Create an "edit-product.html" template
    }

    @PostMapping("/editProduct/{id}")
    public String editProduct(@PathVariable Long id, @ModelAttribute Product updatedProduct) {
        productService.updateProduct(id, updatedProduct);
        return "redirect:/products/admin/allProducts"; // Redirect to the product list page after editing.
    }

    @GetMapping("/addProduct/{productId}")
    public String addItemToCart(@PathVariable("productId") Long productId, Model model){
        cartService.addItemToCart(productId);
        model.addAttribute("products", cartService.getAllProducts());
        return "user/order/products";
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