package com.example.demo.entity;


import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@NoArgsConstructor
@Getter
@Setter
public class OrderItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long orderItemId;
    private Long orderId;
    private Long itemId;


    private int amount;



    public OrderItem(Long orderId,Long itemId, int amount) {
        this.orderId = orderId;
        this.itemId = itemId;
        this.amount = amount;
    }

}
