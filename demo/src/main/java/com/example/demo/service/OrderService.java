package com.example.demo.service;

import com.example.demo.entity.Order;

import java.util.List;

public interface OrderService {

    List<Order> findAllOrders();
    Order findById(Long id);
    Order createOrder(Order order);
    String deleteOrder(Long id);


}
