package com.example.demo.controller;

import com.example.demo.dto.OrderDto;
import com.example.demo.service.CartService;
import com.example.demo.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/order")
public class OrderController {

private final OrderService orderService;
    private final CartService cartService;
@Autowired
    public OrderController(OrderService orderService, CartService cartService) {
    this.orderService = orderService;
    this.cartService = cartService;
    }

    @GetMapping("/cart")
    public String showCart(){
        return "cartView";
    }

    @GetMapping("/increase/{productId}")
    public String increaseProduct(@PathVariable("productId") Long productId){
    cartService.addItemToCart(productId);
    return "cartView";
    }

    @GetMapping("/decrease/{productId}")
    public String decreaseProduct(@PathVariable("productId") Long productId){
        cartService.decreaseItem(productId);
        return "cartView";
    }

    @GetMapping("/remove/{productId}")
    public String removeProductFromCart(@PathVariable("productId") Long productId){
    cartService.removeProduct(productId);
    return "cartView";
    }

    @GetMapping("/summary")
    public String showSummary(){
    return "summary";
    }

    @PostMapping("/saveorder")
    public String saveOrder(OrderDto orderDto){
    orderService.saveOrder(orderDto);
    return "redirect:/products";
        }
    }
