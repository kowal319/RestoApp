package com.example.demo.service;

import com.example.demo.entity.CreditCard;

import java.util.List;
import java.util.Optional;

public interface CreditCardService {
    CreditCard createCard(CreditCard creditCard);

    List<CreditCard> findAllCreditCards();

    Optional<CreditCard> findById(Long id);

    void deleteCreditCard(Long id);
}
