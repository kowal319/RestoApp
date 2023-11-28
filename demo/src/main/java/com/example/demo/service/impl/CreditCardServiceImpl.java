package com.example.demo.service.impl;


import com.example.demo.entity.CreditCard;
import com.example.demo.entity.User;
import com.example.demo.repository.CreditCardRepository;
import com.example.demo.repository.UserRepository;
import com.example.demo.service.CreditCardService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CreditCardServiceImpl implements CreditCardService {

    private final CreditCardRepository creditCardRepository;
    private final UserRepository userRepository;

    public CreditCardServiceImpl(CreditCardRepository creditCardRepository, UserRepository userRepository) {
        this.creditCardRepository = creditCardRepository;
        this.userRepository = userRepository;
    }

    @Override
    public CreditCard createCard(CreditCard creditCard) {
        return creditCardRepository.save(creditCard);
    }

    @Override
    public List<CreditCard> findAllCreditCards() {
        return creditCardRepository.findAll();
    }

    @Override
    public Optional<CreditCard> findById(Long id) {
        return creditCardRepository.findById(id);
    }

    @Override
    public void deleteCreditCard(Long id) {
        // Find the credit card by ID
        Optional<CreditCard> optionalCreditCard = creditCardRepository.findById(id);

        if (optionalCreditCard.isPresent()) {
            CreditCard creditCard = optionalCreditCard.get();

            // Remove the credit card from the user entity
            User user = creditCard.getUser();
            user.setCreditCard(null); // Assuming 'creditCard' is the field name in the User entity

            // Save the user entity to update the relationship
            userRepository.save(user);

            // Delete the credit card
            creditCardRepository.deleteById(id);
        }
    }
}
