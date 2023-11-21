package com.example.demo.controller;

import com.example.demo.dto.ChangePasswordDTO;
import com.example.demo.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

// ChangePasswordController.java
@Controller
@RequestMapping("/users")
public class ChangePasswordController {

    private final UserService userService;

    @Autowired
    public ChangePasswordController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/changePassword/{id}")
    public String showChangePasswordForm(@PathVariable Long id, Model model) {
        ChangePasswordDTO changePasswordDTO = new ChangePasswordDTO();
        model.addAttribute("userId", id);
        model.addAttribute("changePasswordDTO", changePasswordDTO);
        return "user/change-password";
    }

    @PostMapping("/changePassword/{id}")
    public String changePassword(@PathVariable Long id, @ModelAttribute("changePasswordDTO") ChangePasswordDTO changePasswordDTO) {
        userService.changePassword(id, changePasswordDTO);
        return "redirect:/users/profile";
    }
}
