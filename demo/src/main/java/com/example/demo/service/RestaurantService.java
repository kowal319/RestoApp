package com.example.demo.service;

import com.example.demo.entity.Restaurant;

import java.util.List;

public interface RestaurantService {

    List<Restaurant> findAllRestaurants();
    Restaurant findById(Long id);
    Restaurant createRestaurant(Restaurant restaurant);
    String deleteRestaurant(Long id);
    Restaurant updateRestaurant(Long id, Restaurant updateRestaurant);


//    void increaseTables(Long id);
//
//    void decreaseTables(Long id);


    List<Integer> tablesList(Long restaurantId);
}
