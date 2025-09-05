package com.innowise.salescustomeranalysis.model;

import lombok.*;

/**
 * Represents a single item within a customer's order.
 * <p>
 * Each order item contains product details, quantity, price, and the category it belongs to.
 */
@EqualsAndHashCode
@Getter
@AllArgsConstructor
public class OrderItem {

  /**
   * Name of the product.
   */
  private String productName;

  /**
   * Quantity of the product ordered.
   */
  private int quantity;

  /**
   * Price of a single unit of the product.
   */
  private double price;

  /**
   * Category of the product.
   */
  private Category category;
}
