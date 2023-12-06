package com.example.demo.service;

import com.example.demo.entity.PaymentMethod;

import java.util.List;
import java.util.Optional;

public interface PaymentMethodService {
    List<PaymentMethod> findAllPaymentMethods();

    PaymentMethod findById(Long id);
}
