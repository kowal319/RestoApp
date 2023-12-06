package com.example.demo.repository;

import com.example.demo.entity.Order;
import com.example.demo.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {

    List<Order> findByUser(User user);

    @Query("SELECT COALESCE(SUM(oi.amount * p.price), 0) " +
            "FROM Order o " +
            "JOIN OrderItem oi ON o.orderId = oi.orderId " +
            "JOIN Product p ON oi.itemId = p.id " +
            "WHERE o.orderId = :orderId")
    Optional<Double> findOrderTotalSum(@Param("orderId") Long orderId);

    // Method to calculate the total price of an order by orderId
    @Query("SELECT SUM(oi.amount * p.price) FROM OrderItem oi " +
            "JOIN Product p ON oi.itemId = p.id " +
            "WHERE oi.orderId = :orderId")
    Optional<Double> calculateTotalPriceByOrderId(@Param("orderId") Long orderId);





}
