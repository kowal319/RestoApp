package com.example.demo.service;

import com.example.demo.dto.OrderDto;
import com.example.demo.entity.CreditCard;
import com.example.demo.entity.Order;
import com.example.demo.entity.User;
import jakarta.servlet.http.HttpSession;
import org.springframework.security.core.Authentication;

import java.util.List;

public interface OrderService {


    void saveOrder(OrderDto orderDto);

    void saveOrderPaypalSuccess(OrderDto orderDto);

    List<Order> findAllOrders();

    Order findById(Long id);

    List<Order> findOrdersByCurrentUser(Authentication authentication);

    CreditCard findCreditCardByOrderId(Long orderId);

    Double getTotalSumInCart(Long orderId);

    List<Order> findOrdersByUserId(Long userId);

    Double calculateTotalPriceByOrderId(Long orderId);

    Order updatePaidOrder(Long id, Order updatePaidOrder);
}
