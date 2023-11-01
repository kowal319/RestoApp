package com.example.demo.service;

import com.example.demo.dto.RegistrationDto;
import com.example.demo.entity.User;

import java.util.List;
import java.util.Optional;

public interface UserService {
    List<User> findAllUsers();

    User findById(Long id);

    //    User createUser(User user);
    String deleteUser(Long id);

    User updateUser(Long id, User updateUser);

    User findByEmail(String email);

    void saveUser(RegistrationDto registrationDto);
}