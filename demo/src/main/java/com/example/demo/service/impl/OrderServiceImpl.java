package com.example.demo.service.impl;

import com.example.demo.Cart;
import com.example.demo.OrderMapper;
import com.example.demo.dto.OrderDto;
import com.example.demo.entity.*;
import com.example.demo.repository.OrderItemRepository;
import com.example.demo.repository.OrderRepository;
import com.example.demo.service.OrderService;
import com.example.demo.service.PaymentMethodService;
import com.example.demo.service.RestaurantService;
import com.example.demo.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Service
public class OrderServiceImpl implements OrderService {

    private final OrderRepository orderRepository;
    private final OrderItemRepository orderItemRepository;
    private final Cart cart;
    private final RestaurantService restaurantService;
    private final UserService userService;
    private final PaymentMethodService paymentMethodService;
    @Autowired
    public OrderServiceImpl(OrderRepository orderRepository, OrderItemRepository orderItemRepository,
                            Cart cart, RestaurantService restaurantService, UserService userService,
                            PaymentMethodService paymentMethodService) {
        this.orderRepository = orderRepository;
        this.orderItemRepository = orderItemRepository;
        this.cart = cart;
        this.restaurantService = restaurantService;
        this.userService = userService;
        this.paymentMethodService = paymentMethodService;
    }

    @Override
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

    PaymentMethod paymentMethod = paymentMethodService.findById(orderDto.getPaymentMethodId());
    order.setPaymentMethod(paymentMethod);
    order.setPaid("unpaid");
    orderRepository.save(order);
    cart.clearCart();
}

@Override
    public void saveOrderPaypalSuccess(OrderDto orderDto) {
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

        PaymentMethod paymentMethod = paymentMethodService.findById(orderDto.getPaymentMethodId());
        order.setPaymentMethod(paymentMethod);
    order.setPaid("paid");
        orderRepository.save(order);
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
            return orderRepository.findByUser(currentUser);
        } else {
            return orderRepository.findAll();
        }
    }
@Override
    public CreditCard findCreditCardByOrderId(Long orderId) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new RuntimeException("Order not found"));

        return order.getUser().getCreditCard();
    }

    private boolean hasRole(User user, String roleName) {
        return user.getRoles().stream()
                .anyMatch(role -> role.getName().equals(roleName));
    }



    @Override
    public Double getTotalSumInCart(Long orderId) {
        return orderRepository.findOrderTotalSum(orderId).orElse(0.0);
    }


    @Override
    public List<Order> findOrdersByUserId(Long userId) {
        User user = userService.findById(userId);
        if (user != null) {
            return orderRepository.findByUser(user);
        }
        return Collections.emptyList();
    }

    @Override
    public Double calculateTotalPriceByOrderId(Long orderId) {
        return orderRepository.calculateTotalPriceByOrderId(orderId).orElse(0.0);
    }


    @Override
    public Order updatePaidOrder(Long id, Order updatePaidOrder) {
        Optional<Order> optionalOrder = orderRepository.findById(id);
        if (optionalOrder.isPresent()) {
            Order existingOrder = optionalOrder.get();
            existingOrder.setPaid(updatePaidOrder.getPaid());
                return orderRepository.save(existingOrder);
        } else {
            return null;
        }
    }
}
