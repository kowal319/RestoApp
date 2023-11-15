package com.example.demo.service.impl;

import com.example.demo.Cart;
import com.example.demo.OrderMapper;
import com.example.demo.dto.OrderDto;
import com.example.demo.entity.Order;
import com.example.demo.entity.OrderItem;
import com.example.demo.entity.User;
import com.example.demo.repository.OrderItemRepository;
import com.example.demo.repository.OrderRepository;
import com.example.demo.service.OrderService;
import com.example.demo.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class OrderServiceImpl implements OrderService {

    private final OrderRepository orderRepository;
    private final OrderItemRepository orderItemRepository;
    private final Cart cart;

    private final UserService userService;

    @Autowired
    public OrderServiceImpl(OrderRepository orderRepository, OrderItemRepository orderItemRepository, Cart cart, UserService userService) {
        this.orderRepository = orderRepository;
        this.orderItemRepository = orderItemRepository;
        this.cart = cart;
        this.userService = userService;
    }

//    @Override
//    public void saveOrder(OrderDto orderDto){
//        Order order = OrderMapper.mapToOrder(orderDto);
//        orderRepository.save(order);
//        orderItemRepository.saveAll(OrderMapper.mapToOrderItemList(cart, order));
//        cart.clearCart();
//    }
public void saveOrder(OrderDto orderDto) {
    Order order = OrderMapper.mapToOrder(orderDto);

    // Retrieve the User object based on the user ID
    User user = userService.findById(orderDto.getUserId());

    // Set the User in the Order entity
    order.setUser(user);

    // Convert and save order items
    List<OrderItem> orderItems = OrderMapper.mapToOrderItemList(cart, order);
    order.setOrderItems(orderItems);

    // Save the order
    orderRepository.save(order);

    // Clear the cart after order is saved
    cart.clearCart();
}


}
