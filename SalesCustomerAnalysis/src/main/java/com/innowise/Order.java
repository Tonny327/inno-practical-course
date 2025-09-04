package com.innowise;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
@EqualsAndHashCode
public class Order {
    private String orderId;
    private LocalDateTime orderDate;
    private Customer customer;

}
