package com.example.demo.controller;

import com.example.demo.service.CartService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/order")
public class OrderController {


    private final CartService cartService;
@Autowired
    public OrderController(CartService cartService) {
        this.cartService = cartService;
    }

    @GetMapping("/cart")
    public String showCart(){
        return "cartView";
    }

    @GetMapping("/add/{productId}")
    public String addItemToCart(@PathVariable("productId") Long productId){
    cartService.addItemToCart(productId);
    return "cartView";
    }

}