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
import org.springframework.validation.BindingResult;
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


//    @GetMapping
//    public String showAllUsers(Model model) {
//        List<User> users = userService.findAllUsers();
//        model.addAttribute("users", users);
//        return "admin/user/users";
//    }
@GetMapping
public String showFilteredUsers(@RequestParam(name = "roleFilter", required = false) String roleFilter, Model model) {
    List<User> users;
    model.addAttribute("selectedRole", roleFilter);

    if ("RESET".equals(roleFilter)) {
        // If reset option is selected, show all users
        users = userService.findAllUsers();
    } else if (roleFilter != null && !roleFilter.isEmpty()) {
        // Filter users by role
        users = userService.findUsersByRole(roleFilter);
    } else {
        // If no role filter, show all users
        users = userService.findAllUsers();
    }

    model.addAttribute("users", users);
    return "admin/user/users";
}




    @GetMapping("/customers")
    public String showCustomerList(Model model) {
        List<User> customers = userService.findUsersByRole("CUSTOMER");
        model.addAttribute("customers", customers);
        return "admin/user/customers";
    }

    @GetMapping("/employees")
    public String showEmployeeList(Model model) {
        List<User> employees = userService.findUsersByRole("EMPLOYEE");
        model.addAttribute("employees", employees);
        return "admin/user/employees";
    }

    @GetMapping("/addEmployee")
    public String showAddEmployeeForm(Model model) {
        // populate the model as needed
        model.addAttribute("user", new User());
        return "admin/user/addEmployee";
    }

    @PostMapping("/addEmployee")
    public String addEmployee(@ModelAttribute("user") User user, BindingResult result) {
        // add logic to save the user with the "EMPLOYEE" role
        userService.saveUserWithRole(user, "EMPLOYEE");
        return "redirect:/users";
    }
    @GetMapping("/addCustomer")
    public String showAddCustomerForm(Model model) {
        // populate the model as needed
        model.addAttribute("customer", new User());
        return "admin/user/addCustomer";
    }

    @PostMapping("/addCustomer")
    public String addCustomer(@ModelAttribute("customer") User user, BindingResult result) {
        // add logic to save the user with the "EMPLOYEE" role
        userService.saveUserWithRole(user, "CUSTOMER");
        return "redirect:/users";
    }

    @GetMapping("/{id}/orders")
    public String showUserOrders(@PathVariable Long id, Model model) {
        List<Order> userOrders = orderService.findOrdersByUserId(id);

        userOrders.forEach(order -> {
            Double totalPrice = orderService.calculateTotalPriceByOrderId(order.getOrderId());
            order.setTotalPrice(totalPrice != null ? totalPrice : 0.0);
        });

        model.addAttribute("userOrders", userOrders);
        return "admin/order/userOrders";
    }
    @PostMapping("/save")
          public String createUser(@ModelAttribute("user") User user){
            userService.createUser(user);
            return "redirect:/users";
        }

        @GetMapping("/addUser")
        public String showAddUserPage(Model model){
        model.addAttribute("user", new User());
        return "admin/user/add-user";
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
        return "admin/user/edit-user";
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
        String loggedInUsername = authentication.getName();
        User currentUser = userService.findByUsername(loggedInUsername);
        model.addAttribute("userDetails", currentUser);
        return "user/profile/userDetails";
    }

    @GetMapping("/myProfileEdit/{id}")
    public String myProfileEditForm(@PathVariable Long id, Model model){
        User user = userService.findById(id);
        model.addAttribute("user", user);
        return "user/profile/myProfileEdit";
    }

    @PostMapping("/myProfileEdit/{id}")
    public String myProfileEditSave(@PathVariable Long id, @ModelAttribute User updatedUser){
        userService.updateUser(id, updatedUser);
        return "redirect:/users/profile";
    }


    @GetMapping("ofAgeUser/{id}")
    public String ofAgeUserForm(@PathVariable Long id, Model model){
        User user = userService.findById(id);
        model.addAttribute("user", user);
        return "admin/user/ofAgeUser";
    }

    @PostMapping("/ofAgeUser/{id}")
    public String ofAgeUser(@PathVariable Long id, @ModelAttribute User updateOfAgeUser){
        userService.updateUserOfAge(id, updateOfAgeUser);
        return "redirect:/users";
    }

}


