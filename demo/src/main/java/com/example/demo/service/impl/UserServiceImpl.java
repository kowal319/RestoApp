package com.example.demo.service.impl;

import com.example.demo.entity.User;
import com.example.demo.repository.RoleRepository;
import com.example.demo.repository.UserRepository;
import com.example.demo.service.UserService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private RoleRepository roleRepository;

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


    @Override
    public String deleteUser(Long id) {
        userRepository.deleteById(id);
        return "User with ID " + id + " has been deleted.";
    }

    @Override
    public User updateUser(Long id, User updateUser) {
// Check if the user exists
        Optional<User> optionalUser = userRepository.findById(id);
        if (optionalUser.isPresent()) {
            User existingUser = optionalUser.get();
            existingUser.setFirstName(updateUser.getFirstName());
            existingUser.setLastName(updateUser.getLastName());
            existingUser.setPassword(updateUser.getPassword());
            existingUser.setEmail(updateUser.getEmail());
            existingUser.setPhone(updateUser.getPhone());
            existingUser.setBirth(updateUser.getBirth());

            // Save the updated user
            return userRepository.save(existingUser);
        } else {
            // Handle the case where the user with the given ID is not found
            return null;
        }
        }

    @Override
    public User findByEmail(String email) {
        return userRepository.findByEmail(email);
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

}


