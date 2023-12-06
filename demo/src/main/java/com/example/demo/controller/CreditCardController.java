package com.example.demo.controller;


import com.example.demo.entity.CreditCard;
import com.example.demo.entity.User;
import com.example.demo.service.CreditCardService;
import com.example.demo.service.UserService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
public class CreditCardController {

    private final CreditCardService creditCardService;

    private final UserService userService;

@Autowired
    public CreditCardController(CreditCardService creditCardService, UserService userService) {
        this.creditCardService = creditCardService;
    this.userService = userService;
}

    @GetMapping("/users/profile/userCardDetails")
    public String viewCardDetails(Model model, Authentication authentication) {
        String loggedInUsername = authentication.getName();
        User currentUser = userService.findByUsername(loggedInUsername);
        CreditCard cardDetails = currentUser.getCreditCard();
        model.addAttribute("cardDetails", cardDetails);
        return "user/profile/userCardDetails";
    }

    @PostMapping("/users/profile/addCreditCard")
    public String addCreditCard(@ModelAttribute("creditCard") CreditCard creditCard,
                                Authentication authentication) {
        String loggedInUsername = authentication.getName();
        User currentUser = userService.findByUsername(loggedInUsername);

        // Call the UserService method to add the credit card to the user
        userService.addCreditCard(currentUser, creditCard);

        return "redirect:/users/profile";
    }


    @GetMapping("/users/profile/addCreditCard")
    public String showAddCreditCardForm(Model model){
    model.addAttribute("creditCard", new CreditCard());
    return "user/profile/addCreditCard";
    }

    @GetMapping("/users/profile/userCardDetails/deleteCard/{id}")
    public String deleteCard(@PathVariable("id") Long id) {

        System.out.println("Deleting card with id: " + id);

        creditCardService.deleteCreditCard(id);
    return "redirect:/users/profile/userCardDetails";
    }


}

