package com.example.demo.service.impl;

import com.example.demo.entity.Restaurant;
import com.example.demo.repository.RestaurantRepository;
import com.example.demo.service.RestaurantService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
@Service
public class RestaurantServiceImpl implements RestaurantService {

    private final RestaurantRepository restaurantRepository;

    public RestaurantServiceImpl(RestaurantRepository restaurantRepository) {
        this.restaurantRepository = restaurantRepository;
    }

    @Override
    public List<Restaurant> findAllRestaurants() {
        return restaurantRepository.findAll();
    }

    @Override
    public Restaurant findById(Long id) {
        Optional<Restaurant> restaurantOptional = restaurantRepository.findById(id);
        return restaurantOptional.orElse(null);
    }

    @Override
    public Restaurant createRestaurant(Restaurant restaurant) {
        if(isAddressAlreadyExists(restaurant.getAddress())){
            throw new IllegalArgumentException("Address already exists");
        }
        if(isEmailAlreadyExists(restaurant.getEmail())){
            throw new IllegalArgumentException(("Email already exists"));
        }

        return restaurantRepository.save(restaurant);
    }

    private boolean isAddressAlreadyExists(String address){
        return restaurantRepository.existsByAddress(address);
    }

    private boolean isEmailAlreadyExists(String email){
        return restaurantRepository.existsByEmail(email);
    }

    @Override
    public String deleteRestaurant(Long id) {
        restaurantRepository.deleteById(id);
        return "Restaurant with ID " + id + " has been deleted.";
    }

    @Override
    public Restaurant updateRestaurant(Long id, Restaurant updateRestaurant) {
        Optional<Restaurant> optionalRestaurant = restaurantRepository.findById(id);
    if(optionalRestaurant.isPresent()){
        Restaurant existingRestaurant = optionalRestaurant.get();
        existingRestaurant.setName(updateRestaurant.getName());
        existingRestaurant.setAddress(updateRestaurant.getAddress());
        existingRestaurant.setEmail(updateRestaurant.getEmail());
        existingRestaurant.setPhone(updateRestaurant.getPhone());

        return restaurantRepository.save(existingRestaurant);
    }else {
        return null;
    }
    }
}
