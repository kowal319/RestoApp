package com.example.demo.service.impl;

import com.example.demo.entity.Restaurant;
import com.example.demo.repository.RestaurantRepository;
import com.example.demo.service.RestaurantService;
import jakarta.transaction.Transactional;
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
        return restaurantRepository.save(restaurant);
    }



    @Override
    public String deleteRestaurant(Long id) {
        restaurantRepository.deleteById(id);
        return "Restaurant with ID " + id + " has been deleted.";
    }

    @Override
    @Transactional
    public Restaurant updateRestaurant(Long id, Restaurant updateRestaurant) {
        Optional<Restaurant> optionalRestaurant = restaurantRepository.findById(id);
    if(optionalRestaurant.isPresent()){
        Restaurant existingRestaurant = optionalRestaurant.get();
        existingRestaurant.setName(updateRestaurant.getName());
        existingRestaurant.setAddress(updateRestaurant.getAddress());
        existingRestaurant.setEmail(updateRestaurant.getEmail());
        existingRestaurant.setPhone(updateRestaurant.getPhone());
        existingRestaurant.setTableCount(updateRestaurant.getTableCount()); // Update table count

        System.out.println("Updated Restaurant: " + existingRestaurant);

        return restaurantRepository.save(existingRestaurant);
    }else {
        return null;
    }
    }

    @Override
    public void increaseTables(Long id) {
        Optional<Restaurant> optionalRestaurant = restaurantRepository.findById(id);
        optionalRestaurant.ifPresent(restaurant -> {
            restaurant.setTableCount(restaurant.getTableCount() + 1);
            restaurantRepository.save(restaurant);
        });
    }

    @Override
    public void decreaseTables(Long id) {
        Optional<Restaurant> optionalRestaurant = restaurantRepository.findById(id);
        optionalRestaurant.ifPresent(restaurant -> {
            int currentTableCount = restaurant.getTableCount();
            if (currentTableCount > 0) {
                restaurant.setTableCount(currentTableCount - 1);
                restaurantRepository.save(restaurant);
            }
        });
    }
}
