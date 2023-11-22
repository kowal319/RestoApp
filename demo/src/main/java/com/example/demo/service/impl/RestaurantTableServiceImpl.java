package com.example.demo.service.impl;

import com.example.demo.entity.RestaurantTable;
import com.example.demo.repository.RestaurantTableRepository;
import com.example.demo.service.RestaurantTableService;

import java.util.List;
import java.util.Optional;

public class RestaurantTableServiceImpl implements RestaurantTableService {

    private final RestaurantTableRepository restaurantTableRepository;

    public RestaurantTableServiceImpl(RestaurantTableRepository restaurantTableRepository) {
        this.restaurantTableRepository = restaurantTableRepository;
    }


    @Override
    public List<RestaurantTable> findAllTables() {
        return restaurantTableRepository.findAll();
    }

    @Override
    public RestaurantTable findById(Long id) {
        Optional<RestaurantTable> restaurantTableOptional = restaurantTableRepository.findById(id);
        return  restaurantTableOptional.orElse(null);
    }

    @Override
    public RestaurantTable createTable(RestaurantTable restaurantTable) {
        return restaurantTableRepository.save(restaurantTable);
    }

    @Override
    public String deleteTable(Long id) {
        restaurantTableRepository.deleteById(id);
        return "Table with ID: " + id + " has been deleted";
    }
}
