package com.example.demo.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Getter
@Setter
@Entity
@Builder
@Table(name = "orders")
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long orderId;
    private LocalDateTime created;

    @ManyToOne
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    private User user;

    @OneToMany(cascade = CascadeType.ALL)
    @JoinColumn(name = "orderId")
    private List<OrderItem> orderItems;

    @ManyToOne
    @JoinColumn(name = "restaurant_id", referencedColumnName = "id")
    private Restaurant restaurant;

    @Column(name = "table_number")
    private Integer tableNumber;

    @ManyToOne
    @JoinColumn(name = "payment_method_id", referencedColumnName = "id")
    private PaymentMethod paymentMethod;

    @Column(name = "paid")
    private String paid;
}


