package com.example.demo.controller;

import org.springframework.security.authentication.event.AuthenticationSuccessEvent;
import com.example.demo.dto.OrderDto;
import com.example.demo.entity.User;
import com.example.demo.service.CartService;
import com.example.demo.service.OrderService;
import com.example.demo.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/order")
public class OrderController {

private final OrderService orderService;
    private final CartService cartService;

    private final UserService userService;
@Autowired
    public OrderController(OrderService orderService, CartService cartService, UserService userService) {
    this.orderService = orderService;
    this.cartService = cartService;
    this.userService = userService;
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

//    @PostMapping("/saveorder")
//    public String saveOrder(OrderDto orderDto){
//
//    orderService.saveOrder(orderDto);
//    return "redirect:/products";
//        }


    //class with saving order with user id
    @PostMapping("/saveorder")
    public String saveOrder(OrderDto orderDto) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String loggedInUsername = authentication.getName();

        User currentUser = userService.findByUsername(loggedInUsername);

        if (currentUser == null) {
            return "redirect:/error";
        }

        // Set the current user ID in the OrderDto
        orderDto.setUserId(currentUser.getId());

        orderService.saveOrder(orderDto);
        return "redirect:/products";
    }



}
