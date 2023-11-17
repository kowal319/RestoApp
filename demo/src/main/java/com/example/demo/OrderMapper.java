package com.example.demo;


import com.example.demo.dto.OrderDto;
import com.example.demo.entity.CartItem;
import com.example.demo.entity.Order;
import com.example.demo.entity.OrderItem;
import com.example.demo.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.jaxb.SpringDataJaxb;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class OrderMapper {

    @Autowired
    private ProductService productService;
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

    public List<String> mapOrderItemsToProductNames(List<OrderItem> orderItems) {
        // Use Java Streams to map OrderItems to Product Names
        return orderItems.stream()
                .map(orderItem -> getProductNameById(orderItem.getItemId()))
                .collect(Collectors.toList());
    }


    // This method assumes you have a getProductNameById method in your class.
    // Replace it with the actual method you use to fetch product names by ID.
    public String getProductNameById(Long productId) {

       return productService.getProductNameById(productId);
    }




}
