package com.example.demo.service.impl;

import com.example.demo.entity.Cart;
import com.example.demo.repository.CartRepository;
import com.example.demo.service.CartService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CartServiceImpl implements CartService {

    private final CartRepository cartRepository;

    public CartServiceImpl(CartRepository cartRepository) {
        this.cartRepository = cartRepository;
    }

    @Override
    public List<Cart> findAllCarts() {
        return cartRepository.findAll();
    }

    @Override
    public Cart findById(Long id) {
        Optional<Cart> cartOptional = cartRepository.findById(id);
        return cartOptional.orElse(null);
    }

    @Override
    public Cart createCart(Cart cart) {
        return cartRepository.save(cart);
    }

    @Override
    public String deleteCart(Long id) {
        cartRepository.deleteById(id);
        return "Cart with ID " + id + " has been deleted";
    }
}
