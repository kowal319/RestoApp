package com.example.demo.service;

import com.example.demo.entity.RestaurantTable;

import java.util.List;

public interface RestaurantTableService {

    List<RestaurantTable> findAllTables();

    RestaurantTable findById(Long id);

    RestaurantTable createTable(RestaurantTable restaurantTable);

    String deleteTable(Long id);

}
