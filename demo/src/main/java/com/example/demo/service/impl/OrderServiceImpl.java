package com.example.demo.service.impl;

import com.example.demo.entity.Order;
import com.example.demo.repository.OrderRepository;
import com.example.demo.service.OrderService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class OrderServiceImpl implements OrderService {

    private final OrderRepository orderRepository;

    public OrderServiceImpl(OrderRepository orderRepository) {
        this.orderRepository = orderRepository;
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
    public Order createOrder(Order order) {
        return orderRepository.save(order);
    }

    @Override
    public String deleteOrder(Long id) {
        orderRepository.deleteById(id);
        return "Order with ID " + id + " has been deleted";
    }
}
