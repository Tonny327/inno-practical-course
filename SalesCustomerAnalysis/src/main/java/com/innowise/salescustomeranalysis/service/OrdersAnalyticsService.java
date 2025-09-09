package com.innowise.salescustomeranalysis.service;


import com.innowise.salescustomeranalysis.model.Customer;
import com.innowise.salescustomeranalysis.model.Order;
import com.innowise.salescustomeranalysis.model.OrderItem;
import com.innowise.salescustomeranalysis.model.OrderStatus;
import lombok.*;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;


/**
 * Provides analytical operations on a collection of orders.
 * <p>
 * This service contains methods for calculating statistics such as
 * total income, most popular products, average checks, and customer activity.
 */
@AllArgsConstructor
public class OrdersAnalyticsService {

    /**
     * List of orders to be analyzed.
     */
    private final List<Order> orders;

    /**
     * Retrieves a list of unique cities from the provided orders.
     *
     * @param orders the list of orders to analyze
     * @return a list of distinct city names where customers reside
     */
    public List<String> getUniqueCities(List<Order> orders) {
        return orders.stream()
                .map(order -> order.getCustomer().getCity())
                .filter(Objects::nonNull)
                .distinct().collect(Collectors.toList());
    }

    /**
     * Calculates the total income from all delivered orders.
     *
     * @param orders the list of orders to analyze
     * @return the total income as {@link BigDecimal}
     */
    public BigDecimal getTotalIncomeForCompletedOrders(List<Order> orders){
        return orders.stream()
            .filter(order -> order.getStatus() == OrderStatus.DELIVERED)
            .flatMap(order -> order.getItems().stream())
            .map(item -> BigDecimal.valueOf(item.getQuantity()).multiply(item.getPrice()))
            .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    /**
     * Finds the most popular product among delivered orders based on quantity sold.
     *
     * @param orders the list of orders to analyze
     * @return the name of the most popular product, or {@code null} if none found
     */
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

    /**
     * Calculates the average check amount for delivered orders.
     *
     * @param orders the list of orders to analyze
     * @return the average check as {@link BigDecimal}, or {@link BigDecimal#ZERO} if no delivered orders exist
     */
    public BigDecimal getAverageCheckForDeliveredOrders(List<Order> orders) {
        BigDecimal totalSum = BigDecimal.ZERO;
        int deliveredOrdersCount = 0;

        for (Order order : orders) {
            if (order.getStatus() == OrderStatus.DELIVERED && !order.getItems().isEmpty()) {
                deliveredOrdersCount++;
                BigDecimal orderTotal = order.getItems().stream()
                    .map(item -> BigDecimal.valueOf(item.getQuantity()).multiply(item.getPrice()))
                    .reduce(BigDecimal.ZERO, BigDecimal::add);
                totalSum = totalSum.add(orderTotal);
            }
        }

        if (deliveredOrdersCount == 0) {
            return BigDecimal.ZERO;
        }

        return totalSum.divide(BigDecimal.valueOf(deliveredOrdersCount), 2, RoundingMode.HALF_UP);
    }

    /**
     * Retrieves customers who have placed more than five orders.
     *
     * @param orders the list of orders to analyze
     * @return a list of customers with more than five orders
     */
    public List<Customer> getCustomersWithMoreThanFiveOrders(List<Order> orders) {
        Map<Customer, Long> customerOrderCounts = orders.stream()
                .collect(Collectors.groupingBy(
                        Order::getCustomer,
                        Collectors.counting()
                ));

        return customerOrderCounts.entrySet().stream()
                .filter(entry -> entry.getValue() > 5)
                .map(Map.Entry::getKey)
                .collect(Collectors.toList());
    }
}
