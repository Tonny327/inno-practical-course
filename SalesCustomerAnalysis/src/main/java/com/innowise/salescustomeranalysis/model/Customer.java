package com.innowise.salescustomeranalysis.model;

import lombok.*;

import java.time.LocalDateTime;

/**
 * Represents a customer in the sales and order analytics system.
 * <p>
 * A customer contains personal and contact information, as well as registration details, which are
 * used for analytics and reporting.
 */
@Getter
@AllArgsConstructor
@EqualsAndHashCode
public class Customer {

  /**
   * Unique identifier of the customer.
   */
  private String customerId;

  /**
   * Full name of the customer.
   */
  private String name;

  /**
   * Email address of the customer.
   */
  private String email;

  /**
   * Date and time when the customer registered in the system.
   */
  private LocalDateTime registeredAt;

  /**
   * City where the customer resides.
   */
  private String city;

  /**
   * Age of the customer in years.
   */
  private int age;
}
