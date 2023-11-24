package com.example.demo.service.impl;

import com.example.demo.Cart;
import com.example.demo.OrderMapper;
import com.example.demo.dto.OrderDto;
import com.example.demo.entity.Order;
import com.example.demo.entity.OrderItem;
import com.example.demo.entity.Restaurant;
import com.example.demo.entity.User;
import com.example.demo.repository.OrderItemRepository;
import com.example.demo.repository.OrderRepository;
import com.example.demo.service.OrderService;
import com.example.demo.service.RestaurantService;
import com.example.demo.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class OrderServiceImpl implements OrderService {

    private final OrderRepository orderRepository;
    private final OrderItemRepository orderItemRepository;
    private final Cart cart;

    private final RestaurantService restaurantService;

    private final UserService userService;

    @Autowired
    public OrderServiceImpl(OrderRepository orderRepository, OrderItemRepository orderItemRepository, Cart cart, RestaurantService restaurantService, UserService userService) {
        this.orderRepository = orderRepository;
        this.orderItemRepository = orderItemRepository;
        this.cart = cart;
        this.restaurantService = restaurantService;
        this.userService = userService;
    }

    // Save order without user, restaurant and table
//    @Override
//    public void saveOrder(OrderDto orderDto){
//        Order order = OrderMapper.mapToOrder(orderDto);
//        orderRepository.save(order);
//        orderItemRepository.saveAll(OrderMapper.mapToOrderItemList(cart, order));
//        cart.clearCart();
//    }
public void saveOrder(OrderDto orderDto) {
    Order order = OrderMapper.mapToOrder(orderDto);
    User user = userService.findById(orderDto.getUserId());
    order.setUser(user);

    List<OrderItem> orderItems = OrderMapper.mapToOrderItemList(cart, order);
    order.setOrderItems(orderItems);

    Restaurant restaurant = restaurantService.findById(orderDto.getRestaurantId());
    order.setRestaurant(restaurant);
    order.setTableNumber(orderDto.getTableNumber());


    System.out.println("Order before save: " + order);
    System.out.println("OrderItems before save: " + orderItems);

    orderRepository.save(order);

    System.out.println("Order after save: " + order);
    System.out.println("OrderItems after save: " + orderItems);

    cart.clearCart();
}

    @Override
    public List<Order> findAllOrders() {
        return orderRepository.findAll();
    }

    @Override
    public Order findById(Long id) {
        Optional<Order> orderOptional = orderRepository.findById(id);
        return orderOptional.orElse(null);
    }

    @Override
    public List<Order> findOrdersByCurrentUser(Authentication authentication) {
        User currentUser = userService.getCurrentUser(authentication);

        if (hasRole(currentUser, "CUSTOMER")) {
            // Fetch orders only for the current customer
            return orderRepository.findByUser(currentUser);
        } else {
            // Fetch all orders for admin or employee
            return orderRepository.findAll();
        }
    }

    private boolean hasRole(User user, String roleName) {
        return user.getRoles().stream()
                .anyMatch(role -> role.getName().equals(roleName));
    }

    @Override
    public Double getTotalSumInCart(Long orderId) {
        return orderRepository.findOrderTotalSum(orderId).orElse(0.0);
    }


}
