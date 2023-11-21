package com.example.demo.controller;

import com.example.demo.OrderMapper;
import com.example.demo.entity.Order;
import com.example.demo.dto.OrderDto;
import com.example.demo.entity.OrderItem;
import com.example.demo.entity.User;
import com.example.demo.repository.OrderItemRepository;
import com.example.demo.service.CartService;
import com.example.demo.service.OrderService;
import com.example.demo.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/order")
public class OrderController {

private final OrderService orderService;
    private final CartService cartService;

    private final UserService userService;

    private final OrderItemRepository orderItemRepository;

    private final OrderMapper orderMapper;
@Autowired
    public OrderController(OrderService orderService, CartService cartService, UserService userService, OrderItemRepository orderItemRepository, OrderMapper orderMapper) {
    this.orderService = orderService;
    this.cartService = cartService;
    this.userService = userService;
    this.orderItemRepository = orderItemRepository;
    this.orderMapper = orderMapper;
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

//    @GetMapping("/allOrders")
//    public String viewAllOrders(Model model){
//    List<Order> orders = orderService.findAllOrders();
//    model.addAttribute("orders", orders);
//    return "allOrders";
//    }

    @GetMapping("/myOrders")
    public String viewMyOrders(Model model, Authentication authentication) {
        List<Order> orders = orderService.findOrdersByCurrentUser(authentication);
        model.addAttribute("orders", orders);
        return "myOrders";
    }

    @GetMapping("/admin/allOrders")
    public String viewAllOrders(Model model) {
        List<Order> orders = orderService.findAllOrders();
        model.addAttribute("orders", orders);
        return "admin/allOrders";
    }


    private boolean hasRole(User user, String roleName) {
        return user.getRoles().stream()
                .anyMatch(role -> role.getName().equals(roleName));
    }


    @GetMapping("/orderDetails/{id}")
    public String viewOrderDetails(@PathVariable Long id, Model model) {
        // Fetch the order and order items using your service or repository
        Order order = orderService.findById(id);


        // Map order items to a list of product names
        List<String> productNames = orderMapper.mapOrderItemsToProductNames(order.getOrderItems());


        Double totalSumInCart = orderService.getTotalSumInCart(id);
        model.addAttribute("totalSumInCart", totalSumInCart);
        model.addAttribute("productPrices", orderMapper.mapOrderItemsToProductPrices(order.getOrderItems()));



        // Add data to the model
        model.addAttribute("order", order);
        model.addAttribute("productNames", productNames);

        // Return the Thymeleaf template name
        return "orderDetails";
    }



}
