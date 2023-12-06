package com.example.demo.service.impl;

import com.example.demo.entity.PaymentMethod;
import com.example.demo.repository.PaymentMethodRepository;
import com.example.demo.service.PaymentMethodService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class PaymentMethodServiceImpl  implements PaymentMethodService {


    private final PaymentMethodRepository paymentMethodRepository;

    public PaymentMethodServiceImpl(PaymentMethodRepository paymentMethodRepository) {
        this.paymentMethodRepository = paymentMethodRepository;
    }


    @Override
    public List<PaymentMethod> findAllPaymentMethods(){
        return paymentMethodRepository.findAll();
    }

    @Override
    public PaymentMethod findById(Long id) {
        Optional<PaymentMethod> paymentMethodOptional = paymentMethodRepository.findById(id);
return paymentMethodOptional.orElse(null);
    }
}
