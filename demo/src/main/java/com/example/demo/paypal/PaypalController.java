package com.example.demo.paypal;


import com.example.demo.Cart;
import com.example.demo.controller.OrderController;
import com.example.demo.dto.OrderDto;
import com.example.demo.entity.Order;
import com.example.demo.service.OrderService;
import com.paypal.api.payments.Links;
import com.paypal.api.payments.Payment;
import com.paypal.base.rest.PayPalRESTException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
public class PaypalController {

    @Autowired
    PaypalService service;

    @Autowired
    private Cart cart;

    public static final String SUCCESS_URL = "pay/success";
    public static final String CANCEL_URL = "pay/paymentCanceled";

    @GetMapping("/paymentConfirmation")
    public String home() {
        return "paymentConfirmation";
    }

    @PostMapping("/pay")
    public String payment(@ModelAttribute("order") Order order, Model model) {
        Double total = cart.getSum();
        model.addAttribute("cart", cart);
        try {
            Payment payment = service.createPayment(total, "PLN", "paypal", "sale", "no dsc",
                    "http://localhost:8080/" + CANCEL_URL,
                    "http://localhost:8080/" + SUCCESS_URL);
            for(Links link:payment.getLinks()) {
                if(link.getRel().equals("approval_url")) {
                    return "redirect:"+link.getHref();
                }
            }

        } catch (PayPalRESTException e) {
            e.printStackTrace();
        }
        return "redirect:/";
    }

    @GetMapping(value = CANCEL_URL)
    public String cancelPay() {
        return "paymentCanceled";
    }

    @GetMapping(value = SUCCESS_URL)
    public String successPay(@RequestParam("paymentId") String paymentId, @RequestParam("PayerID") String payerId) {
        try {
            Payment payment = service.executePayment(paymentId, payerId);
            if (payment.getState().equals("approved")) {
                return "success";
            }
        } catch (PayPalRESTException e) {
//            System.out.println(e.getMessage());
        }
        return "redirect:/";
    }


}