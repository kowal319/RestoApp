package com.example.demo;


import com.example.demo.entity.CartItem;
import com.example.demo.entity.Product;
import lombok.Getter;
import lombok.Setter;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@Component
@Scope(value = "session", proxyMode = ScopedProxyMode.TARGET_CLASS)
public class Cart {
    private List<CartItem> cartItems = new ArrayList<>();
     public int counter = 0;
     public double sum = 0;


    public void addProduct(Product product) {
        boolean notFound = true;

        for (CartItem ci : cartItems) {
            if (ci.getProduct().getId().equals(product.getId())) {
                notFound = false;
                ci.increaseCounter();
                recalculatePriceAndCounter();
                break;
            }
        }
        if (notFound) {
            cartItems.add(new CartItem(product));
            recalculatePriceAndCounter();

        }
    }

    public void removeProduct(Product product) {
        for (CartItem ci : cartItems) {
            if (ci.getProduct().getId().equals(product.getId())) {
                ci.decreaseCounter();
                if (ci.hasZeroProduct()) {
                    cartItems.remove(ci);
                }
                recalculatePriceAndCounter();
                break;
            }
        }
    }

    private void recalculatePriceAndCounter(){
        int tempCounter = 0;
        double tempPrice = 0;
        for(CartItem ci : cartItems){
            tempCounter += ci.getCounter();
            tempPrice = tempPrice + ci.getPrice();
        }
        this.counter = tempCounter;
        this.sum = tempPrice;
    }
}
