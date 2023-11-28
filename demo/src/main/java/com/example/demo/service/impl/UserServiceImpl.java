package com.example.demo.service.impl;

import com.example.demo.dto.ChangePasswordDTO;
import com.example.demo.dto.RegistrationDto;
import com.example.demo.entity.CreditCard;
import com.example.demo.entity.Role;
import com.example.demo.entity.User;
import com.example.demo.repository.RoleRepository;
import com.example.demo.repository.UserRepository;
import com.example.demo.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;

@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private RoleRepository roleRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public UserServiceImpl(UserRepository userRepository, RoleRepository roleRepository) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
    }


    @Override
    public List<User> findAllUsers() {
        return userRepository.findAll();
    }

    @Override
    public User findById(Long id) {
        Optional<User> userOptional = userRepository.findById(id);
        return userOptional.orElse(null);
    }

    @Override
    public User createUser(User user) {
        // Perform additional logic/validation before saving
//        if (isUsernameAlreadyExists(user.getUsername())) {
//            throw new IllegalArgumentException("Username already exists");
//        }
//
//        if (isEmailAlreadyExists(user.getEmail())) {
//            throw new IllegalArgumentException("Email already exists");
//        }

        // If validation passes, save the user
        return userRepository.save(user);
    }


//    @Override
//    public String deleteUser(Long id) {
//        userRepository.deleteById(id);
//        return "User with ID " + id + " has been deleted.";
//    }

    @Override
    public String deleteUser(Long id) {
        Optional<User> optionalUser = userRepository.findById(id);
        if (optionalUser.isPresent()) {
            User user = optionalUser.get();

            // Remove roles associated with the user
            user.getRoles().clear();
            userRepository.save(user);

            // Delete the user
            userRepository.deleteById(id);

            return "User with ID " + id + " has been deleted.";
        } else {
            return "User with ID " + id + " not found.";
        }
    }


    @Override
    public User updateUser(Long id, User updateUser) {
// Check if the user exists
        Optional<User> optionalUser = userRepository.findById(id);
        if (optionalUser.isPresent()) {
            User existingUser = optionalUser.get();
            existingUser.setFirstName(updateUser.getFirstName());
            existingUser.setLastName(updateUser.getLastName());
//            existingUser.setPassword(updateUser.getPassword());
            existingUser.setUsername(updateUser.getUsername());
            existingUser.setPhone(updateUser.getPhone());
            // Save the updated user
            return userRepository.save(existingUser);
        } else {
            // Handle the case where the user with the given ID is not found
            return null;
        }
        }

    @Override
    public User findByUsername(String username) {
        return userRepository.findByUsername(username);
    }


@Override
    public User getCurrentUser(Authentication authentication) {
        if (authentication != null && authentication.isAuthenticated()) {
            String username = authentication.getName();
            // Fetch and return the User object based on the username
            return userRepository.findByUsername(username);
        }
        return null; // Or throw an exception, depending on your needs
    }

    @Override
    public User registerUser(RegistrationDto registrationDto) {
        User user = new User();
        user.setFirstName(registrationDto.getFirstName());
        user.setLastName(registrationDto.getLastName());
        user.setUsername(registrationDto.getUsername());
        user.setPassword(passwordEncoder.encode(registrationDto.getPassword()));
        user.setPhone(registrationDto.getPhone());

        // Set default role as "customer"
        Role customerRole = roleRepository.findByName("CUSTOMER");
        user.setRoles(new HashSet<>(Collections.singletonList(customerRole)));

        return userRepository.save(user);
    }

//    @Override
//        public void saveUser(RegistrationDto registrationDto) {
//            User user = new User();
//            user.setName(registrationDto.getFirstName() + " " + registrationDto.getLastName());
//            user.setEmail(registrationDto.getEmail());
//            //use spring security to encrypt password
//            user.setPassword(passwordEncoder.encode(registrationDto.getPassword()));
//            Role role = roleRepository.findByName("ROLE_GUEST");
//            user.setRoles(Arrays.asList(role));
//            userRepository.save(user);
//        }

    @Override
    public void changePassword(Long userId, ChangePasswordDTO changePasswordDTO) {
        User user = userRepository.findById(userId).orElseThrow(() -> new RuntimeException("User not found"));

        if (passwordEncoder.matches(changePasswordDTO.getOldPassword(), user.getPassword())
                && changePasswordDTO.getNewPassword().equals(changePasswordDTO.getConfirmPassword())) {

            user.setPassword(passwordEncoder.encode(changePasswordDTO.getNewPassword()));
            userRepository.save(user);
        } else {
            throw new RuntimeException("Invalid old password or new password confirmation.");
        }
    }


    @Override
    public void addCreditCard(User user, CreditCard creditCard) {
        user.setCreditCard(creditCard);
        userRepository.save(user);
    }

}


