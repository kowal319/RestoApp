package com.example.demo.service.impl;

import com.example.demo.Cart;
import com.example.demo.OrderMapper;
import com.example.demo.dto.OrderDto;
import com.example.demo.entity.Order;
import com.example.demo.repository.OrderItemRepository;
import com.example.demo.repository.OrderRepository;
import com.example.demo.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class OrderServiceImpl implements OrderService {

    private final OrderRepository orderRepository;
    private final OrderItemRepository orderItemRepository;
    private final Cart cart;

    @Autowired
    public OrderServiceImpl(OrderRepository orderRepository, OrderItemRepository orderItemRepository, Cart cart) {
        this.orderRepository = orderRepository;
        this.orderItemRepository = orderItemRepository;
        this.cart = cart;
    }

    @Override
    public void saveOrder(OrderDto orderDto){
        Order order = OrderMapper.mapToOrder(orderDto);
        orderRepository.save(order);
        orderItemRepository.saveAll(OrderMapper.mapToOrderItemList(cart, order));
        cart.clearCart();
    }


}
