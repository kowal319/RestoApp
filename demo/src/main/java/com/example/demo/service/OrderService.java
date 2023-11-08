package com.example.demo.service;

import com.example.demo.dto.OrderDto;
import com.example.demo.entity.Order;

import java.util.List;

public interface OrderService {


    void saveOrder(OrderDto orderDto);
}
