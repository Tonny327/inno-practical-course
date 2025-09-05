package com.innowise.salescustomeranalysis.service;

import com.innowise.salescustomeranalysis.util.TestDataGenerator;
import com.innowise.salescustomeranalysis.model.Customer;
import com.innowise.salescustomeranalysis.model.Order;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for {@link OrdersAnalyticsService}.
 * <p>
 * This test class verifies the correctness of analytical methods
 * such as retrieving unique cities, calculating total income,
 * finding the most popular product, computing average checks,
 * and identifying customers with more than five orders.
 */
class OrdersAnalyticsServiceTest {

  private OrdersAnalyticsService serviceSample;
  private OrdersAnalyticsService serviceExtended;
  private List<Order> sampleOrders;
  private List<Order> extendedOrders;

  /**
   * Initializes test data before each test case.
   */
  @BeforeEach
  void setUp() {
    sampleOrders = TestDataGenerator.generateSampleOrders();
    extendedOrders = TestDataGenerator.generateExtendedOrders();
    serviceSample = new OrdersAnalyticsService(sampleOrders);
    serviceExtended = new OrdersAnalyticsService(extendedOrders);
  }

  /**
   * Verifies that unique cities are correctly extracted from sample orders.
   */
  @Test
  void testGetUniqueCities_sample() {
    List<String> cities = serviceSample.getUniqueCities(sampleOrders);
    assertTrue(cities.contains("Minsk"));
    assertTrue(cities.contains("Warsaw"));
    assertTrue(cities.contains("Berlin"));
    assertTrue(cities.contains("Pinsk"));
    assertTrue(cities.contains("Gomel"));
    assertTrue(cities.contains("Vitebsk"));
    assertEquals(6, cities.size());
  }

  /**
   * Verifies that total income is correctly calculated for delivered orders in sample data.
   */
  @Test
  void testGetTotalIncomeForCompletedOrders_sample() {
    BigDecimal total = serviceSample.getTotalIncomeForCompletedOrders(sampleOrders);
    assertEquals(0, total.compareTo(new BigDecimal("3305")));
  }

  /**
   * Verifies that the most popular product is correctly identified in sample data.
   */
  @Test
  void testGetMostPopularProduct_sample() {
    String product = serviceSample.getMostPopularProduct(sampleOrders);
    assertEquals("Toy Car", product);
  }

  /**
   * Verifies that the average check is correctly calculated for delivered orders in sample data.
   */
  @Test
  void testGetAverageCheckForDeliveredOrders_sample() {
    BigDecimal avg = serviceSample.getAverageCheckForDeliveredOrders(sampleOrders);
    assertEquals(new BigDecimal("275.42"), avg);
  }

  /**
   * Verifies that customers with more than five orders are correctly identified in sample data.
   */
  @Test
  void testGetCustomersWithMoreThanFiveOrders_sample() {
    List<Customer> result = serviceSample.getCustomersWithMoreThanFiveOrders(sampleOrders);
    assertEquals(1, result.size());
    assertEquals("customer5", result.get(0).getName());
  }

  /**
   * Verifies that unique cities are correctly extracted from extended orders.
   */
  @Test
  void testGetUniqueCities_extended() {
    List<String> cities = serviceExtended.getUniqueCities(extendedOrders);
    assertTrue(cities.contains("Vilnius"));
    assertTrue(cities.contains("Minsk"));
    assertTrue(cities.contains("Riga"));
    assertTrue(cities.contains("Kaunas"));
    assertTrue(cities.contains("Tallinn"));
    assertEquals(5, cities.size());
  }

  /**
   * Verifies that total income is correctly calculated for delivered orders in extended data.
   */
  @Test
  void testGetTotalIncomeForCompletedOrders_extended() {
    BigDecimal total = serviceExtended.getTotalIncomeForCompletedOrders(extendedOrders);
    assertEquals(0, total.compareTo(new BigDecimal("1260")));
  }

  /**
   * Verifies that the most popular product is correctly identified in extended data.
   */
  @Test
  void testGetMostPopularProduct_extended() {
    String product = serviceExtended.getMostPopularProduct(extendedOrders);
    assertTrue(product.equals("Mouse") || product.equals("Keyboard"));
  }

  /**
   * Verifies that the average check is correctly calculated for delivered orders in extended data.
   */
  @Test
  void testGetAverageCheckForDeliveredOrders_extended() {
    BigDecimal avg = serviceExtended.getAverageCheckForDeliveredOrders(extendedOrders);
    assertEquals(new BigDecimal("114.55"), avg);
  }

  /**
   * Verifies that customers with more than five orders are correctly identified in extended data.
   */
  @Test
  void testGetCustomersWithMoreThanFiveOrders_extended() {
    List<Customer> result = serviceExtended.getCustomersWithMoreThanFiveOrders(extendedOrders);
    assertEquals(1, result.size());
    assertEquals("customer7", result.get(0).getName());
  }

  /**
   * Verifies that all analytical methods return correct results for an empty orders list.
   */
  @Test
  void testEmptyOrdersList() {
    OrdersAnalyticsService emptyService = new OrdersAnalyticsService(Collections.emptyList());

    assertTrue(emptyService.getUniqueCities(Collections.emptyList()).isEmpty());
    assertEquals(BigDecimal.ZERO,
        emptyService.getTotalIncomeForCompletedOrders(Collections.emptyList()));
    assertNull(emptyService.getMostPopularProduct(Collections.emptyList()));
    assertEquals(BigDecimal.ZERO,
        emptyService.getAverageCheckForDeliveredOrders(Collections.emptyList()));
    assertTrue(emptyService.getCustomersWithMoreThanFiveOrders(Collections.emptyList()).isEmpty());
  }
}



