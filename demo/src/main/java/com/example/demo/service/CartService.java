package com.example.demo.service;

import com.example.demo.entity.Cart;

import java.util.List;

public interface CartService {

    List<Cart> findAllCarts();

    Cart findById(Long id);
    Cart createCart(Cart cart);
    String deleteCart(Long id);


}
