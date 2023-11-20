package com.example.demo.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class HomeController {

    @GetMapping("/login")
    public String shoHome(){
        return "login";
    }

    @PostMapping("/login")
    public String login() {
        // Your login logic here
        return "redirect:/products"; // Redirect to the products page after successful login
    }

    @GetMapping("/home")
    public String homePage() {
        // Your login logic here
        return "home"; // Redirect to the products page after successful login
    }
}
