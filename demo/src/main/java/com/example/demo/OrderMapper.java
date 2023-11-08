package com.example.demo;


import com.example.demo.dto.OrderDto;
import com.example.demo.entity.CartItem;
import com.example.demo.entity.Order;
import com.example.demo.entity.OrderItem;
import org.springframework.data.domain.jaxb.SpringDataJaxb;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class OrderMapper {
    public static Order mapToOrder(OrderDto orderDto){
return Order.builder().created(LocalDateTime.now()).build();
    }
    public static List<OrderItem> mapToOrderItemList(Cart cart, Order order){
        List<OrderItem> orderItems = new ArrayList<>();
        for(CartItem ci : cart.getCartItems()){
            orderItems.add(new OrderItem(order.getOrderId(), ci.getProduct().getId(), ci.getCounter()));
        }
        return orderItems;
    }
}
