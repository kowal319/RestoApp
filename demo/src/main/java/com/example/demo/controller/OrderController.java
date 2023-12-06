package com.example.demo.controller;

import com.example.demo.OrderMapper;
import com.example.demo.entity.*;
import com.example.demo.dto.OrderDto;
import com.example.demo.repository.OrderItemRepository;
import com.example.demo.service.*;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;


@Controller
@RequestMapping("/order")
public class OrderController {

private final OrderService orderService;
    private final CartService cartService;

    private final UserService userService;

    private final OrderItemRepository orderItemRepository;

    private final OrderMapper orderMapper;

    private final RestaurantService restaurantService;

    private final PaymentMethodService paymentMethodService;
@Autowired
    public OrderController(OrderService orderService, CartService cartService, UserService userService, OrderItemRepository orderItemRepository, OrderMapper orderMapper, RestaurantService restaurantService, PaymentMethodService paymentMethodService) {
    this.orderService = orderService;
    this.cartService = cartService;
    this.userService = userService;
    this.orderItemRepository = orderItemRepository;
    this.orderMapper = orderMapper;
    this.restaurantService = restaurantService;
    this.paymentMethodService = paymentMethodService;
}

    @GetMapping("/cart")
    public String showCart(){

    return "user/order/cartView";
    }

    @GetMapping("/increase/{productId}")
    public String increaseProduct(@PathVariable("productId") Long productId){
    cartService.addItemToCart(productId);
    return "user/order/cartView";
    }

    @GetMapping("/decrease/{productId}")
    public String decreaseProduct(@PathVariable("productId") Long productId){
        cartService.decreaseItem(productId);
        return "user/order/cartView";
    }

    @GetMapping("/remove/{productId}")
    public String removeProductFromCart(@PathVariable("productId") Long productId){
    cartService.removeProduct(productId);
    return "user/order/cartView";
    }



    @GetMapping("/summary")
    public String showSummary(Model model, HttpSession session){

        String selectedPaymentMethodName = (String) session.getAttribute("selectedPaymentMethodName");
        Long restaurantId = (Long) session.getAttribute("restaurantId");
        Integer selectedTable = (Integer) session.getAttribute("selectedTable");

        model.addAttribute("restaurantId", restaurantId);
        model.addAttribute("selectedTable", selectedTable);
        model.addAttribute("selectedPaymentMethodName", selectedPaymentMethodName);


        return "user/order/summary";
    }


//  Save order without user, restaurant id and table number
//    @PostMapping("/saveorder")
//    public String saveOrder(OrderDto orderDto){
//    orderService.saveOrder(orderDto);
//    return "redirect:/products";
//        }


    //class with saving order with user id, resutarntId and table number
    @PostMapping("/saveorder")
    public String saveOrder(OrderDto orderDto, HttpSession session) {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String loggedInUsername = authentication.getName();

        User currentUser = userService.findByUsername(loggedInUsername);
        Long restaurantId = (Long) session.getAttribute("restaurantId");
        Integer selectedTable = (Integer) session.getAttribute("selectedTable");
        Long paymentMethodId = (Long) session.getAttribute("paymentMethodId");


        if (currentUser == null) {
            return "redirect:/error";}

        // Set the current user ID, restaurantId and tableNumber in the OrderDto
        orderDto.setUserId(currentUser.getId());
        orderDto.setRestaurantId(restaurantId);
        orderDto.setTableNumber(selectedTable);
        orderDto.setPaymentMethodId(paymentMethodId);


        orderService.saveOrder(orderDto);
        return "redirect:/order/myOrders";
    }

    @GetMapping("/myOrders")
    public String viewMyOrders(Model model, Authentication authentication) {
        List<Order> orders = orderService.findOrdersByCurrentUser(authentication);
        model.addAttribute("orders", orders);
        return "user/order/myOrders";
    }

    @GetMapping("/admin/allOrders")
    public String viewAllOrders(Model model) {
        List<Order> orders = orderService.findAllOrders();
        // Group orders by restaurant and table number
        Map<String, List<Order>> ordersByRestaurantAndTable = orders.stream()
                .filter(order -> order.getRestaurant() != null)
                .collect(Collectors.groupingBy(order -> order.getRestaurant().getName() + ": Table " + order.getTableNumber()));


        model.addAttribute("ordersByRestaurantAndTable", ordersByRestaurantAndTable);

        model.addAttribute("orders", orders);
        return "admin/order/allOrders";
    }


    private boolean hasRole(User user, String roleName) {
        return user.getRoles().stream()
                .anyMatch(role -> role.getName().equals(roleName));
    }

    @GetMapping("/orderDetails/{id}")
    public String viewOrderDetails(@PathVariable Long id, Model model) {
        // Fetch the order and order items using your service or repository
        Order order = orderService.findById(id);

        // Map order items to a list of product names
        List<String> productNames = orderMapper.mapOrderItemsToProductNames(order.getOrderItems());

        Double totalSumInCart = orderService.getTotalSumInCart(id);
        model.addAttribute("totalSumInCart", totalSumInCart);
        model.addAttribute("productPrices", orderMapper.mapOrderItemsToProductPrices(order.getOrderItems()));

        // Add data to the model
        model.addAttribute("order", order);
        model.addAttribute("productNames", productNames);

        // Return the Thymeleaf template name
        return "user/order/orderDetails";
    }
    @GetMapping("/admin/orderDetails/{id}")
    public String viewOrderDetailsAdmin(@PathVariable Long id, Model model) {
        // Fetch the order and order items using your service or repository
        Order order = orderService.findById(id);

        // Map order items to a list of product names
        List<String> productNames = orderMapper.mapOrderItemsToProductNames(order.getOrderItems());

        Double totalSumInCart = orderService.getTotalSumInCart(id);
        model.addAttribute("totalSumInCart", totalSumInCart);
        model.addAttribute("productPrices", orderMapper.mapOrderItemsToProductPrices(order.getOrderItems()));

        // Add data to the model
        model.addAttribute("order", order);
        model.addAttribute("productNames", productNames);

        // Return the Thymeleaf template name
        return "admin/order/orderDetailsAdmin";
    }
    @GetMapping("/admin/orderDetails/{id}/cardDetails")
    public String viewCardDetails(@PathVariable Long id, Model model) {
        // Fetch the order and credit card using your service or repository
        Order order = orderService.findById(id);
        CreditCard creditCard = orderService.findCreditCardByOrderId(id);

        // Add data to the model
        model.addAttribute("order", order);
        model.addAttribute("creditCard", creditCard);
        model.addAttribute("orderId", id); // Add the orderId to the model


        // Return the Thymeleaf template name for card details
        return "admin/order/cardDetails";
    }

    @GetMapping("/choose-paymentMethod")
    public String showChoosePaymentMethodForm(Model model, HttpSession session) {
        // Fetch all restaurants
        List<PaymentMethod> paymentMethods = paymentMethodService.findAllPaymentMethods();
        model.addAttribute("paymentMethods", paymentMethods);
        return "user/order/choose-paymentMethod";
    }

    @PostMapping("/choose-payment")
    public String choosePayment(@RequestParam("paymentMethodId") Long paymentMethodId,
                                HttpSession session) {
        // Save the paymentMethodId in the session for later use
        session.setAttribute("paymentMethodId", paymentMethodId);
        return "redirect:/order/choose-restaurant";
    }

//    @GetMapping("/choose-restaurant")
//    public String showChooseRestaurantForm(@RequestParam("paymentMethodId") Long paymentMethodId,
//                                           Model model, HttpSession session) {
//        model.addAttribute("paymentMethodId", paymentMethodId);
//        List<Restaurant> restaurants = restaurantService.findAllRestaurants();
//        model.addAttribute("restaurants", restaurants);
//        System.out.println("payment method choosen  in getMapping show restaurant form : " + paymentMethodId);
//        return "user/order/choose-restaurant";
//    }
@GetMapping("/choose-restaurant")
public String showChooseRestaurantForm(@RequestParam("paymentMethodId") Long paymentMethodId,
                                       Model model, HttpSession session) {
    session.setAttribute("paymentMethodId", paymentMethodId);
    List<Restaurant> restaurants = restaurantService.findAllRestaurants();
    model.addAttribute("restaurants", restaurants);

    // Fetch the PaymentMethod based on paymentMethodId
    PaymentMethod selectedPaymentMethod = paymentMethodService.findById(paymentMethodId);

    model.addAttribute("selectedPaymentMethodName", selectedPaymentMethod.getName());

    System.out.println("Payment method chosen in getMapping show restaurant form: " + selectedPaymentMethod.getName());

    // Save payment method name in session for later use
    session.setAttribute("selectedPaymentMethodName", selectedPaymentMethod.getName());

    return "user/order/choose-restaurant";
}


    @PostMapping("/choose-restaurant/{id}")
    public String processRestaurantSelection(@RequestParam("restaurantId") Long restaurantId,
                                             @RequestParam("paymentMethodId") Long paymentMethodId,
                                             Model model,
                                             HttpSession session) {

        session.setAttribute("paymentMethodId", paymentMethodId); // Set paymentMethodId in the session
        System.out.println("Payment Method ID saved in session: " + paymentMethodId);

        List<Integer> availableTables = restaurantService.tablesList(restaurantId);
        System.out.println(" 111processRestaurantSelection :Number of available tables: " + availableTables.size());

        Restaurant selectedRestaurant = restaurantService.findById(restaurantId);
        model.addAttribute("selectedRestaurant", selectedRestaurant);

        model.addAttribute("restaurantId", restaurantId);
        model.addAttribute("availableTables", availableTables);

        // Redirect to a page where the user can choose a table
        return "user/order/choose-restaurant/" + restaurantId + "/choose-table";
    }

    @GetMapping("/choose-restaurant/{id}/choose-table")
    public String showChooseTableForm(
                                      @PathVariable Long id, Model model, HttpSession session) {

        Long paymentMethodId = (Long) session.getAttribute("paymentMethodId");
        System.out.println("Retrieved Payment Method ID from session: " + paymentMethodId);

        model.addAttribute("paymentMethodId", paymentMethodId);
        System.out.println("Retrieved Payment Method ID from session: " + paymentMethodId);

        String selectedPaymentMethodName = (String) session.getAttribute("selectedPaymentMethodName");

        // Add the selected payment method name to the model
        model.addAttribute("selectedPaymentMethodName", selectedPaymentMethodName);

        Restaurant restaurant = restaurantService.findById(id);
        model.addAttribute("restaurant", restaurant);
        List<Integer> availableTables = restaurantService.tablesList(id);
        model.addAttribute("availableTables", availableTables);
        return "user/order/choose-table";
    }

    @PostMapping("/choose-restaurant/{id}/choose-table")
    public String processTableSelection(@PathVariable Long id,
                                        @RequestParam("selectedTable") Integer selectedTable,
                                        @ModelAttribute("restaurantId") Long restaurantId,
                                        Model model, HttpSession session
                                       ) {
         session.setAttribute("restaurantId", restaurantId);
         session.setAttribute("selectedTable", selectedTable);

        System.out.println("2222processTableSelection Selected restaurant ID: " + restaurantId);
        System.out.println(" processTableSelection Selected table: " + selectedTable);
        return "redirect:/products";
    }
}