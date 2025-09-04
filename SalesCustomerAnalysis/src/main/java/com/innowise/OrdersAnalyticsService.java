package com.innowise;


import lombok.AllArgsConstructor;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

@AllArgsConstructor
public class OrdersAnalyticsService {
    private final List<Order> orders;

    public List<String> getUniqueCities(List<Order> orders) {
        return orders.stream()
                .map(order -> order.getCustomer().getCity())
                .filter(Objects::nonNull)
                .distinct().collect(Collectors.toList());
    }

    public BigDecimal getTotalIncomeForCompletedOrders(List<Order> orders){
        return orders.stream()
                .filter(order -> order.getStatus() == OrderStatus.DELIVERED)
                .flatMap(order -> order.getItems().stream())
                .map(item -> BigDecimal.valueOf(item.getQuantity()).multiply(BigDecimal.valueOf(item.getPrice())))
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    public String getMostPopularProduct(List<Order> orders){
        Map<String, Integer> productQuantities = orders.stream()
                .filter(order -> order.getStatus() == OrderStatus.DELIVERED)
                .flatMap(order -> order.getItems().stream())
                .collect(Collectors.groupingBy(OrderItem::getProductName,
                        Collectors.summingInt(OrderItem::getQuantity)));

        return productQuantities.entrySet().stream()
                .max(Comparator.comparingInt(Map.Entry::getValue))
                .map(Map.Entry::getKey).orElse(null);
    }




}
