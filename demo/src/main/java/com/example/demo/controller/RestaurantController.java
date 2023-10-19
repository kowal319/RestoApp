package com.example.demo.controller;

import com.example.demo.entity.Restaurant;
import com.example.demo.service.RestaurantService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/restaurants")
public class RestaurantController {

    private final RestaurantService restaurantService;

@Autowired
    public RestaurantController(RestaurantService restaurantService) {
        this.restaurantService = restaurantService;
    }

    @GetMapping
    public List<Restaurant> findAllRestaurants(){
    return restaurantService.findAllRestaurants();
    }

    @PostMapping
    public Restaurant createRestaurant(@RequestBody Restaurant restaurant){
    return restaurantService.createRestaurant(restaurant);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Restaurant> getRestaurantById(@PathVariable Long id){
    Restaurant restaurant = restaurantService.findById(id);
    if(restaurant != null){
        return  ResponseEntity.ok(restaurant);
    } else {
        return ResponseEntity.notFound().build();
    }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteRestaurant(@PathVariable Long id){
    String result = restaurantService.deleteRestaurant(id);
    return  ResponseEntity.ok(result);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Restaurant> updateRestaurant(@PathVariable Long id, @RequestBody Restaurant updateRestaurant){
    Restaurant updatedEntity = restaurantService.updateRestaurant(id, updateRestaurant);
    if(updatedEntity != null){
        return ResponseEntity.ok(updatedEntity);
    }else {
        return ResponseEntity.notFound().build();
    }
    }

}
