package com.example.demo;


import com.example.demo.entity.CartItem;
import com.example.demo.entity.Product;
import lombok.Getter;
import lombok.Setter;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Getter
@Setter
@Component
@Scope(value = "session", proxyMode = ScopedProxyMode.TARGET_CLASS)
public class Cart {
    private List<CartItem> cartItems = new ArrayList<>();
     public int counter = 0;
     public double sum = 0;


    public void addProduct(Product product) {

        getCartItemByItem(product).ifPresentOrElse(
                CartItem::increaseCounter,
                () -> cartItems.add(new CartItem(product))
        );
        recalculatePriceAndCounter();
    }

    public void descreaseProduct(Product product) {
        Optional<CartItem> oCartItem = getCartItemByItem(product);
        if (oCartItem.isPresent()) {
            CartItem cartItem = oCartItem.get();
            cartItem.decreaseCounter();
            if (cartItem.hasZeroProduct()) {
                removeAllProductsFromCart(product);
            }else {
                recalculatePriceAndCounter();

            }
        }
        recalculatePriceAndCounter();
    }

    private void recalculatePriceAndCounter(){
        sum = cartItems.stream().map(CartItem::getPrice)
                .reduce((double) 0, Double::sum);
        counter = cartItems.stream().mapToInt(CartItem::getCounter)
                .reduce(0, (a,b) -> a + b );

        BigDecimal sumDecimal = BigDecimal.valueOf(sum);
        sumDecimal = sumDecimal.setScale(2, RoundingMode.HALF_UP);
        sum = sumDecimal.doubleValue();
    }


    private Optional<CartItem> getCartItemByItem(Product product){
        return cartItems.stream()
                .filter(i -> i.isEqual(product))
                .findFirst();
    }

    public void removeAllProductsFromCart(Product product){
        cartItems.removeIf(i -> i.isEqual(product));
        recalculatePriceAndCounter();
    }

    public void clearCart(){
        cartItems.clear();
        counter = 0;
        sum = 0;

    }
}
