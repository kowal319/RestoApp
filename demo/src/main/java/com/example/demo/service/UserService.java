package com.example.demo.service;

import com.example.demo.dto.ChangePasswordDTO;
import com.example.demo.dto.RegistrationDto;
import com.example.demo.entity.User;
import org.springframework.security.core.Authentication;

import java.util.List;
import java.util.Optional;

public interface UserService {
    List<User> findAllUsers();

    User findById(Long id);

    User createUser(User user);

    //    User createUser(User user);
    String deleteUser(Long id);

    User updateUser(Long id, User updateUser);


    User findByUsername(String username);

    User getCurrentUser(Authentication authentication);

    User registerUser(RegistrationDto registrationDto);
//    void saveUser(RegistrationDto registrationDto);

    void changePassword(Long userId, ChangePasswordDTO changePasswordDTO);

}