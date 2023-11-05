package com.example.demo.entity;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;



@Getter
@Setter
public class CartItem {

    private Product product;
    private int counter;
    private double price;

    public CartItem(Product product) {
        this.product = product;
        this.counter = 1;
        this.price = product.getPrice();
    }

    public void increaseCounter(){
        counter++;
        price = product.getPrice() * counter;
    }

    public void decreaseCounter(){
        if(counter > 0) {
            counter--;
            price = product.getPrice() * counter;

        }
    }


    public boolean hasZeroProduct() {
        return counter == 0;
    }
}
