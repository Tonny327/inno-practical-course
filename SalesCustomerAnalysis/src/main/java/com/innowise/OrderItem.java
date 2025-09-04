package com.innowise;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;

@EqualsAndHashCode
@Getter
@AllArgsConstructor
public class OrderItem {
    private String productName;
    private int quantity;
    private double price;
    private Category category;
}
