package com.example.demo.controller;


import com.example.demo.entity.Order;
import com.example.demo.entity.User;
import com.example.demo.repository.OrderRepository;
import com.example.demo.service.OrderService;
import com.example.demo.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/users")
public class UserController {

    private final UserService userService;

    private final OrderRepository orderRepository;

    private final OrderService orderService;

    @Autowired
    public UserController(UserService userService, OrderRepository orderRepository, OrderService orderService) {
        this.userService = userService;
        this.orderRepository = orderRepository;
        this.orderService = orderService;
    }


    @GetMapping
    public String showAllUsers(Model model) {
        List<User> users = userService.findAllUsers();
        model.addAttribute("users", users);
        return "user/users";
    }

        @PostMapping("/save")
          public String createUser(@ModelAttribute("user") User user){
            userService.createUser(user);
            return "redirect:/users";
        }

        @GetMapping("/addUser")
        public String showAddUserPage(Model model){
        model.addAttribute("user", new User());
        return "user/add-user";
        }
    @GetMapping("/{id}")
    public ResponseEntity<User> getUserById(@PathVariable Long id) {
        User user = userService.findById(id);
        if (user != null) {
            return ResponseEntity.ok(user);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/deleteUser/{id}")
    public String deleteUser(@PathVariable("id") Long id) {
        userService.deleteUser(id);
        return "redirect:/users";
    }

@GetMapping("editUser/{id}")
    public String editUserForm(@PathVariable Long id, Model model){
        User user = userService.findById(id);
        model.addAttribute("user", user);
        return "user/edit-user";
}

@PostMapping("/editUser/{id}")
    public String editProduct(@PathVariable Long id, @ModelAttribute User updatedUser){
        userService.updateUser(id, updatedUser);
        return "redirect:/users";
}


@GetMapping("/showOrders")
public List<Order> showOrders(){
        return orderRepository.findAll();

}

    @GetMapping("/profile")
    public String viewUserProfile(Model model, Authentication authentication) {
        // Retrieve the currently logged-in user for order history
        String loggedInUsername = authentication.getName();
        User currentUser = userService.findByUsername(loggedInUsername);
                model.addAttribute("userDetails", currentUser);

        return "user/userDetails"; // The name of your Thymeleaf template for the profile page
    }




}


