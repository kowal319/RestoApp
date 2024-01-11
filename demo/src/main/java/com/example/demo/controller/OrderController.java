package com.example.demo.controller;

import com.example.demo.Cart;
import com.example.demo.OrderMapper;
import com.example.demo.dto.OrderDto;
import com.example.demo.entity.*;
import com.example.demo.repository.OrderItemRepository;
import com.example.demo.service.*;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
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
    private Cart cart;


    @Autowired
    public OrderController(OrderService orderService, CartService cartService,
                           UserService userService, OrderItemRepository orderItemRepository,
                           OrderMapper orderMapper, RestaurantService restaurantService,
                           PaymentMethodService paymentMethodService) {
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
    public String showSummary(Model model, HttpSession session,
                              @RequestParam("paymentMethodId") Long paymentMethodId){

// Add the user object to the model
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User user = userService.findByUsername(authentication.getName());
        model.addAttribute("user", user);

        //Select payment method
        session.setAttribute("paymentMethodId", paymentMethodId);
  PaymentMethod selectedPaymentMethod = paymentMethodService.findById(paymentMethodId);
model.addAttribute("selectedPaymentMethodName", selectedPaymentMethod.getName());
   session.setAttribute("selectedPaymentMethodName", selectedPaymentMethod.getName());


        String selectedPaymentMethodName = (String) session.getAttribute("selectedPaymentMethodName");
        Long restaurantId = (Long) session.getAttribute("restaurantId");
        Integer selectedTable = (Integer) session.getAttribute("selectedTable");

        Restaurant selectedRestaurant = restaurantService.findById(restaurantId);
        String selectedRestaurantName = selectedRestaurant.getName();

        model.addAttribute("selectedRestaurantName", selectedRestaurantName);

        model.addAttribute("restaurantId", restaurantId);
        model.addAttribute("selectedTable", selectedTable);
        model.addAttribute("selectedPaymentMethodName", selectedPaymentMethodName);

        System.out.println("Selected Payment Method Name from Session: " + session.getAttribute("selectedPaymentMethodName"));
        System.out.println("Payment Method ID from Session: " + session.getAttribute("paymentMethodId"));

        return "user/order/summary";
    }


    @PostMapping("/saveorder")
    public String saveOrder(OrderDto orderDto, HttpSession session) {
    // Common logic
    handleOrderSave(orderDto, session);
    orderService.saveOrder(orderDto);
        return "redirect:/order/myOrders";
}


    @PostMapping("/savepaypal")
    public String saveWithPaypal(OrderDto orderDto, HttpSession session) {
    handleOrderSave(orderDto, session);
    orderService.saveOrderPaypalSuccess(orderDto);
        return "redirect:/order/myOrders";
    }

    private void handleOrderSave(OrderDto orderDto, HttpSession session) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String loggedInUsername = authentication.getName();

        User currentUser = userService.findByUsername(loggedInUsername);
        Long restaurantId = (Long) session.getAttribute("restaurantId");
        Integer selectedTable = (Integer) session.getAttribute("selectedTable");
        Long paymentMethodId = (Long) session.getAttribute("paymentMethodId");


        if (currentUser == null) {
            throw new RuntimeException("User not found"); // Or redirect to an error page
        }

        orderDto.setUserId(currentUser.getId());
        orderDto.setRestaurantId(restaurantId);
        orderDto.setTableNumber(selectedTable);
        orderDto.setPaymentMethodId(paymentMethodId);

//        orderService.saveOrder(orderDto);
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

//    @GetMapping("/choose-paymentMethod")
//    public String showChoosePaymentMethodForm(Model model, HttpSession session) {
//        // Fetch all restaurants
//        List<PaymentMethod> paymentMethods = paymentMethodService.findAllPaymentMethods();
//        model.addAttribute("paymentMethods", paymentMethods);
//        return "user/order/choose-paymentMethod";
//    }
//
//    @PostMapping("/choose-payment")
//    public String choosePayment(@RequestParam("paymentMethodId") Long paymentMethodId,
//                                HttpSession session) {
//        // Save the paymentMethodId in the session for later use
//        session.setAttribute("paymentMethodId", paymentMethodId);
//        return "redirect:/order/choose-restaurant";
//    }
@GetMapping("/choose-paymentMethod")
public String showChoosePaymentMethodForm(Model model, HttpSession session) {
    // Fetch all restaurants
    List<PaymentMethod> paymentMethods = paymentMethodService.findAllPaymentMethods();
    model.addAttribute("paymentMethods", paymentMethods);
    return "user/order/choose-paymentMethod";
}
    @PostMapping("/choose-payment/{id}")
    public String choosePayment(@RequestParam("paymentMethodId") Long paymentMethodId,
                                HttpSession session, Model model) {
        // Retrieve the selected payment method
        System.out.println("Payment Method ID: " + paymentMethodId);

        PaymentMethod selectedPaymentMethod = paymentMethodService.findById(paymentMethodId);

        // Save both the ID and the name in the session for later use
        session.setAttribute("paymentMethodId", paymentMethodId);
        session.setAttribute("selectedPaymentMethodName", selectedPaymentMethod.getName());

        model.addAttribute("selectedPaymentMethodName", selectedPaymentMethod.getName());
        model.addAttribute("paymentMethodId", paymentMethodId);

        return "redirect:/user/order/summary";  // Redirect to the summary page
    }

//    @GetMapping("/choose-restaurant")
//    public String showChooseRestaurantForm(@RequestParam("paymentMethodId") Long paymentMethodId,
//                                       Model model, HttpSession session) {
//    session.setAttribute("paymentMethodId", paymentMethodId);
//    List<Restaurant> restaurants = restaurantService.findAllRestaurants();
//    model.addAttribute("restaurants", restaurants);
//
//    // Fetch the PaymentMethod based on paymentMethodId
//    PaymentMethod selectedPaymentMethod = paymentMethodService.findById(paymentMethodId);
//
//    model.addAttribute("selectedPaymentMethodName", selectedPaymentMethod.getName());
//    // Save payment method name in session for later use
//    session.setAttribute("selectedPaymentMethodName", selectedPaymentMethod.getName());
//
//    return "user/order/choose-restaurant";
//}
@GetMapping("/choose-restaurant")
public String showChooseRestaurantForm(
                                       Model model, HttpSession session) {
    List<Restaurant> restaurants = restaurantService.findAllRestaurants();
    model.addAttribute("restaurants", restaurants);
    return "user/order/choose-restaurant";
}

@PostMapping("/choose-restaurant/{id}")
public String processRestaurantSelection(@RequestParam("restaurantId") Long restaurantId,
                                         Model model) {
    List<Integer> availableTables = restaurantService.tablesList(restaurantId);

    Restaurant selectedRestaurant = restaurantService.findById(restaurantId);
    model.addAttribute("selectedRestaurant", selectedRestaurant);

    model.addAttribute("restaurantId", restaurantId);
    model.addAttribute("availableTables", availableTables);
    return "user/order/choose-restaurant/" + restaurantId + "/choose-table";
}

//
    //    @PostMapping("/choose-restaurant/{id}")
//    public String processRestaurantSelection(@RequestParam("restaurantId") Long restaurantId,
//                                             @RequestParam("paymentMethodId") Long paymentMethodId,
//                                             Model model,
//                                             HttpSession session) {
//
//        session.setAttribute("paymentMethodId", paymentMethodId);
//
//        List<Integer> availableTables = restaurantService.tablesList(restaurantId);
//
//        Restaurant selectedRestaurant = restaurantService.findById(restaurantId);
//        model.addAttribute("selectedRestaurant", selectedRestaurant);
//
//        model.addAttribute("restaurantId", restaurantId);
//        model.addAttribute("availableTables", availableTables);
//
//        // Redirect to a page where the user can choose a table
//        return "user/order/choose-restaurant/" + restaurantId + "/choose-table";
//    }
//    @GetMapping("/choose-restaurant/{id}/choose-table")
//    public String showChooseTableForm(
//                                      @PathVariable Long id, Model model, HttpSession session) {
//
//        Long paymentMethodId = (Long) session.getAttribute("paymentMethodId");
//
//        model.addAttribute("paymentMethodId", paymentMethodId);
//
//        String selectedPaymentMethodName = (String) session.getAttribute("selectedPaymentMethodName");
//
//        // Add the selected payment method name to the model
//        model.addAttribute("selectedPaymentMethodName", selectedPaymentMethodName);
//
//        Restaurant restaurant = restaurantService.findById(id);
//        model.addAttribute("restaurant", restaurant);
//        List<Integer> availableTables = restaurantService.tablesList(id);
//        model.addAttribute("availableTables", availableTables);
//        return "user/order/choose-table";
//    }
@GetMapping("/choose-restaurant/{id}/choose-table")
public String showChooseTableForm(
        @PathVariable Long id, Model model, HttpSession session) {
    Restaurant restaurant = restaurantService.findById(id);
    String selectedRestaurantName = restaurant.getName();
    model.addAttribute("restaurant", restaurant);
    List<Integer> availableTables = restaurantService.tablesList(id);
    model.addAttribute("availableTables", availableTables);

    model.addAttribute("selectedRestaurantName", selectedRestaurantName);
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

        return "redirect:/products";
    }

    @PostMapping("/proceedToPayment")
    public String proceedToPayment(Model model) {
        // Add the cart information to the model
        model.addAttribute("cart", cart);
        // Return the payment confirmation page
        return "paymentConfirmation";
    }

    @GetMapping("/updatePaid/{id}")
    public String updatePaidForm(@PathVariable Long id, Model model) {
        Order order = orderService.findById(id);
        model.addAttribute("order", order);
        return "admin/order/updatePaidOrder"; // Create an "edit-product.html" template
    }

    @PostMapping("/updatePaid/{id}")
    public String updatePaid(@PathVariable Long id, @ModelAttribute Order updatedPaidOrder) {
        orderService.updatePaidOrder(id, updatedPaidOrder);
        return "redirect:/order/admin/allOrders"; // Redirect to the product list page after editing.
    }

    @GetMapping("/employee/allOrders")
    public String viewAllOrdersEmployee(Model model) {
        List<Order> orders = orderService.findAllOrders();
        model.addAttribute("orders", orders);
        return "admin/order/allOrders";
    }

    @GetMapping("/employee/choose-restaurant")
    public String showChooseRestaurantForm(Model model) {
        List<Restaurant> restaurants = restaurantService.findAllRestaurants();
        model.addAttribute("restaurants", restaurants);
        return "user/order/choose-restaurant";
    }

    @PostMapping("/employee/choose-restaurant/{id}")
    public String processRestaurantSelection(@RequestParam("restaurantId") Long restaurantId,
                                             Model model,
                                             HttpSession session) {
        Restaurant selectedRestaurant = restaurantService.findById(restaurantId);
        model.addAttribute("restaurantId", restaurantId);
        return "employee/choose-restaurant/" + restaurantId + "/orders";
    }


}