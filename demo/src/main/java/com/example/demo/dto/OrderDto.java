package com.example.demo.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Getter
@Setter
public class OrderDto {

private Long userId;
    private Long restaurantId;
    private Integer tableNumber;

    private Long paymentMethodId;
    private Double totalAmount;

    private String currency;  // Add this field for currency selection

    private String method;
    private String intent;
    private String description;

}

