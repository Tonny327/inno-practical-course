package com.innowise.salescustomeranalysis.model;

/**
 * Represents the current processing state of an order.
 * <p>
 * The order status is used to track the progress of an order from creation to completion or
 * cancellation.
 */
public enum OrderStatus {

  /**
   * The order has been created but not yet processed.
   */
  NEW,

  /**
   * The order is currently being prepared or handled.
   */
  PROCESSING,

  /**
   * The order has been shipped to the customer but not yet delivered.
   */
  SHIPPED,

  /**
   * The order has been successfully delivered to the customer.
   */
  DELIVERED,

  /**
   * The order has been cancelled and will not be processed further.
   */
  CANCELLED
}
