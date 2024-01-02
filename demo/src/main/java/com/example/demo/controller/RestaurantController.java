package com.example.demo.controller;

import com.example.demo.entity.Restaurant;
import com.example.demo.service.RestaurantService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/restaurants")
public class RestaurantController {

    private final RestaurantService restaurantService;

@Autowired
    public RestaurantController(RestaurantService restaurantService) {
        this.restaurantService = restaurantService;
    }

    @GetMapping
    public String findAllRestaurants(Model model){
    List<Restaurant>  restaurants = restaurantService.findAllRestaurants();
    model.addAttribute("restaurants", restaurants);
    return "admin/restaurant/admin-all-restaurants";
    }
    @GetMapping("/employee")
    public String findAllRestaurantsEmployee(Model model){
        List<Restaurant>  restaurants = restaurantService.findAllRestaurants();
        model.addAttribute("restaurants", restaurants);
        return "employee/restaurants";
    }
    @PostMapping("/save")
    public String createRestaurant(@ModelAttribute("restaurant") Restaurant restaurant)
    {
        restaurantService.createRestaurant(restaurant);
        return "redirect:/restaurants";
    }

    @GetMapping("/addRestaurant")
    public String showAddRestaurantPage(Model model){
    model.addAttribute("restaurant", new Restaurant());
    return "admin/restaurant/add-restaurant";
    }

    @GetMapping("/{id}")
    public ResponseEntity<Restaurant> getRestaurantById(@PathVariable Long id){
    Restaurant restaurant = restaurantService.findById(id);
    if(restaurant != null){
        return ResponseEntity.ok(restaurant);
    } else{
        return ResponseEntity.notFound().build();
    }
    }

    @GetMapping("/deleteRestaurant/{id}")
    public String deleteRestaurant(@PathVariable("id") Long id){
    restaurantService.deleteRestaurant(id);
    return "redirect:/restaurants";
    }

    @GetMapping("/editRestaurant/{id}")
    public String editRestaurantForm(@PathVariable Long id, Model model){
        Restaurant restaurant = restaurantService.findById(id);
        int tableCount = restaurant.getTableCount();  // Adjust this based on your Restaurant class

        model.addAttribute("restaurant", restaurant);
        model.addAttribute("tableCount", tableCount); // Add this line

        return "admin/restaurant/edit-restaurant";
    }

    @PostMapping("/editRestaurant/{id}")
    public String editRestaurant(@PathVariable Long id, @ModelAttribute Restaurant updatedRestaurant){
    restaurantService.updateRestaurant(id, updatedRestaurant);
        // Manually fetch the restaurant after the update
        Restaurant fetchedRestaurant = restaurantService.findById(id);
        return "redirect:/restaurants";
    }

//    @GetMapping("/increaseTables/{id}")
//    public String increaseTables(@PathVariable Long id) {
//        restaurantService.increaseTables(id);
//        return "redirect:/restaurants/editRestaurant/" + id;
//    }
//
//    @GetMapping("/decreaseTables/{id}")
//    public String decreaseTables(@PathVariable Long id) {
//        restaurantService.decreaseTables(id);
//        return "redirect:/restaurants/editRestaurant/" + id;
//    }

}
