package com.innowise.salescustomeranalysis.util;

import com.innowise.salescustomeranalysis.model.Category;
import com.innowise.salescustomeranalysis.model.Customer;
import com.innowise.salescustomeranalysis.model.Order;
import com.innowise.salescustomeranalysis.model.OrderItem;
import com.innowise.salescustomeranalysis.model.OrderStatus;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.math.BigDecimal;

/**
 * Utility class for generating sample and extended test data for the sales and order analytics
 * system.
 * <p>
 * This class provides factory methods for creating customers, orders, and order items, as well as
 * predefined datasets for unit testing.
 */
public class TestDataGenerator {

  /**
   * Creates a new {@link Customer} instance with the specified parameters.
   *
   * @param id   unique customer identifier
   * @param name full name of the customer
   * @param city city where the customer resides
   * @param age  age of the customer in years
   * @return a new {@link Customer} object
   */
  public static Customer createCustomer(String id, String name, String city, int age) {
    return new Customer(
        id,
        name,
        name.toLowerCase() + "@mail.com",
        LocalDateTime.now().minusDays(30),
        city,
        age
    );
  }

  /**
   * Creates a new {@link OrderItem} instance with the specified parameters.
   *
   * @param name     name of the product
   * @param quantity quantity of the product
   * @param price    price per unit of the product
   * @param category category of the product
   * @return a new {@link OrderItem} object
   */
  public static OrderItem createOrderItem(String name, int quantity, double price,
      Category category) {
    return new OrderItem(name, quantity, BigDecimal.valueOf(price), category);
  }

  /**
   * Creates a new {@link Order} instance with the specified parameters.
   *
   * @param id       unique order identifier
   * @param customer the customer who placed the order
   * @param status   current status of the order
   * @param items    one or more order items
   * @return a new {@link Order} object
   */
  public static Order createOrder(String id, Customer customer, OrderStatus status,
      OrderItem... items) {
    return new Order(
        id,
        LocalDateTime.now().minusDays((long) (Math.random() * 10)),
        customer,
        Arrays.asList(items),
        status
    );
  }

  /**
   * Generates a basic dataset of orders for simple test scenarios.
   * <p>
   * The dataset includes multiple customers with varying numbers of orders and different order
   * statuses.
   *
   * @return a list of sample {@link Order} objects
   */
  public static List<Order> generateSampleOrders() {
    List<Order> orders = new ArrayList<>();

    Customer customer1 = createCustomer("C1", "customer1", "Minsk", 28);
    Customer customer2 = createCustomer("C2", "customer2", "Warsaw", 35);
    Customer customer3 = createCustomer("C3", "customer3", "Berlin", 42);
    Customer customer4 = createCustomer("C4", "customer4", "Pinsk", 42);
    Customer customer5 = createCustomer("C5", "customer5", "Gomel", 42);
    Customer customer6 = createCustomer("C6", "customer6", "Vitebsk", 42);

    orders.add(createOrder("O1", customer1, OrderStatus.DELIVERED,
        createOrderItem("iPhone", 1, 1000, Category.ELECTRONICS),
        createOrderItem("Charger", 2, 20, Category.ELECTRONICS)));

    orders.add(createOrder("O2", customer2, OrderStatus.NEW,
        createOrderItem("T-Shirt", 3, 25, Category.CLOTHING)));

    orders.add(createOrder("O3", customer1, OrderStatus.DELIVERED,
        createOrderItem("Book", 3, 15, Category.BOOKS),
        createOrderItem("Laptop", 1, 1200, Category.ELECTRONICS)));

    orders.add(createOrder("O4", customer3, OrderStatus.DELIVERED,
        createOrderItem("Perfume", 1, 80, Category.BEAUTY)));

    orders.add(createOrder("O5", customer4, OrderStatus.DELIVERED,
        createOrderItem("TV", 1, 500, Category.ELECTRONICS),
        createOrderItem("Remote", 1, 50, Category.ELECTRONICS)));

    orders.add(createOrder("O6", customer4, OrderStatus.CANCELLED,
        createOrderItem("Headphones", 1, 100, Category.ELECTRONICS)));

    orders.add(createOrder("O7", customer4, OrderStatus.DELIVERED,
        createOrderItem("Desk", 1, 150, Category.HOME)));

    for (int i = 1; i <= 6; i++) {
      orders.add(createOrder("C5-O" + i, customer5, OrderStatus.DELIVERED,
          createOrderItem("Toy Car", 1, 30, Category.TOYS)));
    }
    orders.add(createOrder("O8", customer6, OrderStatus.SHIPPED,
        createOrderItem("Camera", 1, 400, Category.ELECTRONICS)));

    orders.add(createOrder("O9", customer6, OrderStatus.DELIVERED,
        createOrderItem("Backpack", 1, 60, Category.CLOTHING)));

    return orders;
  }

  /**
   * Generates an extended dataset of orders for advanced test scenarios.
   * <p>
   * The dataset includes a different set of customers compared to the sample data, a wider variety
   * of products, and different order statuses to cover edge cases.
   *
   * @return a list of extended {@link Order} objects
   */
  public static List<Order> generateExtendedOrders() {
    List<Order> orders = new ArrayList<>();

    Customer customer7 = createCustomer("C7", "customer7", "Vilnius", 30);
    Customer customer8 = createCustomer("C8", "customer8", "Minsk", 22);
    Customer customer9 = createCustomer("C9", "customer9", "Riga", 27);
    Customer customer10 = createCustomer("C10", "customer10", "Kaunas", 40);
    Customer customer11 = createCustomer("C11", "customer11", "Tallinn", 34);

    for (int i = 1; i <= 6; i++) {
      orders.add(createOrder("D1" + i, customer7, OrderStatus.DELIVERED,
          createOrderItem("Mouse", 1, 25, Category.ELECTRONICS),
          createOrderItem("Keyboard", 1, 45, Category.ELECTRONICS)));
    }

    orders.add(createOrder("E1", customer8, OrderStatus.CANCELLED,
        createOrderItem("Shoes", 1, 70, Category.CLOTHING)));
    orders.add(createOrder("E2", customer8, OrderStatus.DELIVERED,
        createOrderItem("Shoes", 2, 70, Category.CLOTHING)));
    orders.add(createOrder("E3", customer8, OrderStatus.DELIVERED));
    orders.add(createOrder("E4", customer8, OrderStatus.SHIPPED,
        createOrderItem("Tablet", 1, 300, Category.ELECTRONICS)));

    orders.add(createOrder("R1", customer9, OrderStatus.NEW,
        createOrderItem("Monitor", 1, 200, Category.ELECTRONICS)));
    orders.add(createOrder("R2", customer9, OrderStatus.DELIVERED,
        createOrderItem("Chair", 1, 150, Category.HOME)));
    orders.add(createOrder("R3", customer9, OrderStatus.DELIVERED,
        createOrderItem("Book", 4, 15, Category.BOOKS)));

    orders.add(createOrder("K1", customer10, OrderStatus.CANCELLED,
        createOrderItem("Phone Case", 2, 10, Category.ELECTRONICS)));

    orders.add(createOrder("T1", customer11, OrderStatus.DELIVERED,
        createOrderItem("Coffee Machine", 1, 250, Category.HOME)));
    orders.add(createOrder("T2", customer11, OrderStatus.DELIVERED,
        createOrderItem("Headphones", 2, 120, Category.ELECTRONICS)));

    return orders;
  }
}

