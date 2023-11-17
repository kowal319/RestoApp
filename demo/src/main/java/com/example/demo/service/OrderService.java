package com.example.demo.service;

import com.example.demo.dto.OrderDto;
import com.example.demo.entity.Order;
import com.example.demo.entity.User;
import org.springframework.security.core.Authentication;

import java.util.List;

public interface OrderService {


    void saveOrder(OrderDto orderDto);


    List<Order> findAllOrders();

    Order findById(Long id);

    List<Order> findOrdersByCurrentUser(Authentication authentication);
}
