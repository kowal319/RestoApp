package com.example.demo.controller;

import com.example.demo.Cart;
import com.example.demo.dto.OrderDto;
import com.example.demo.entity.Category;
import com.example.demo.entity.Product;
import com.example.demo.entity.Restaurant;
import com.example.demo.service.CartService;
import com.example.demo.service.ProductService;
import com.example.demo.service.RestaurantService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
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
    private final RestaurantService restaurantService;

    @Autowired
    public ProductController(ProductService productService, Cart cart, CartService cartService, RestaurantService restaurantService) {
        this.productService = productService;
        this.cart = cart;
        this.cartService = cartService;
        this.restaurantService = restaurantService;
    }


@GetMapping()
public String viewAllProducts(Model model, HttpSession session) {
    List<Product> products = productService.findAllProducts();
    List<Category> categories = productService.getAllCategories();

    Long restaurantId = (Long) session.getAttribute("restaurantId");
    Integer selectedTable = (Integer) session.getAttribute("selectedTable");


    model.addAttribute("products", products);
    model.addAttribute("restaurantId", restaurantId);
    model.addAttribute("selectedTable", selectedTable);
    model.addAttribute("categories", categories);


//    Long paymentMethodId = (Long) session.getAttribute("paymentMethodId"); // Retrieve paymentMethodId
//
//    model.addAttribute("paymentMethodId", paymentMethodId); // Add paymentMethodId to the model
//
//    String selectedPaymentMethodName = (String) session.getAttribute("selectedPaymentMethodName");

Restaurant selectedRestaurant = restaurantService.findById(restaurantId);
String selectedRestaurantName = selectedRestaurant.getName();

    model.addAttribute("selectedRestaurantName", selectedRestaurantName);

    // Add the selected payment method name to the model
//    model.addAttribute("selectedPaymentMethodName", selectedPaymentMethodName);



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
        model.addAttribute("categories", productService.getAllCategories());

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
        model.addAttribute("categories", productService.getAllCategories());

        return "admin/product/edit-product"; // Create an "edit-product.html" template
    }

    @PostMapping("/editProduct/{id}")
    public String editProduct(@PathVariable Long id, @ModelAttribute Product updatedProduct) {
        productService.updateProduct(id, updatedProduct);
        return "redirect:/products/admin/allProducts"; // Redirect to the product list page after editing.
    }


    //Here under I added http session to save restaurant id and selectedtable,
    @GetMapping("/addProduct/{productId}")
    public String addItemToCart(@PathVariable("productId") Long productId, Model model, OrderDto orderDto, HttpSession session){
        cartService.addItemToCart(productId);
        List<Category> categories = productService.getAllCategories();
        model.addAttribute("products", cartService.getAllProducts());

        Long restaurantId = (Long) session.getAttribute("restaurantId");
        Integer selectedTable = (Integer) session.getAttribute("selectedTable");

        model.addAttribute("restaurantId", restaurantId);
        model.addAttribute("selectedTable", selectedTable);
        model.addAttribute("categories", categories);
        return "user/order/products";
    }



}