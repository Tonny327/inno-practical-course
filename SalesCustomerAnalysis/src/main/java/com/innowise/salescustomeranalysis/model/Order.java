package com.innowise.salescustomeranalysis.model;

import lombok.*;


import java.time.LocalDateTime;
import java.util.List;

/**
 * Represents a customer order in the sales and order analytics system.
 * <p>
 * An order contains information about the purchase date, the customer who placed it, the list of
 * ordered items, and the current order status.
 */
@Getter
@AllArgsConstructor
@EqualsAndHashCode
public class Order {

  /**
   * Unique identifier of the order.
   */
  private String orderId;

  /**
   * Date and time when the order was placed.
   */
  private LocalDateTime orderDate;

  /**
   * The customer who placed the order.
   */
  private Customer customer;

  /**
   * List of items included in the order.
   */
  private List<OrderItem> items;

  /**
   * Current status of the order.
   */
  private OrderStatus status;
}
