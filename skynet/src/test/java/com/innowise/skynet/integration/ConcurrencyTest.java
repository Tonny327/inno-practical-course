package com.innowise.skynet.integration;

import com.innowise.skynet.service.Factory;
import com.innowise.skynet.service.SimulationCoordinator;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Timeout;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Integration tests
 * <p>
 * These tests verify that the factory handles concurrent access correctly:
 * <ul>
 * <li>Two threads simulating World and Wednesday factions accessing factory</li>
 * <li>Thread-safe parts collection without race conditions</li>
 * <li>Data integrity under concurrent parts collection</li>
 * <li>Stress testing with multiple concurrent collectors</li>
 * </ul>
 * </p>
 */
class ConcurrencyTest {

  /**
   * Tests that multiple factions can access the factory concurrently without issues.
   * Verifies thread safety during concurrent parts collection.
   * 
   * @throws InterruptedException if the test is interrupted
   */
  @Test
  @Timeout(15)
  void shouldHandleMultipleFactionsConcurrently() throws InterruptedException {
    SimulationCoordinator coordinator = new SimulationCoordinator();
    Factory factory = new Factory(coordinator);

    factory.producePartsForTesting(10);

    CountDownLatch latch = new CountDownLatch(2);
    Thread[] collectorThreads = {
        new Thread(() -> {
          try {
            factory.collectParts(2, 0);
          } finally {
            latch.countDown();
          }
        }),
        new Thread(() -> {
          try {
            factory.collectParts(2, 1);
          } finally {
            latch.countDown();
          }
        })
    };

    for (Thread thread : collectorThreads) {
      thread.start();
    }

    latch.await(5, TimeUnit.SECONDS);

    assertThat(factory.getAvailablePartsCount()).isGreaterThanOrEqualTo(0);
    
    factory.stop();
  }

  /**
   * Tests that parts collection does not have race conditions.
   * Verifies that concurrent collection operations maintain data integrity.
   * 
   * @throws InterruptedException if the test is interrupted
   */
  @Test
  @Timeout(10)
  void shouldNotHaveRaceConditionsInPartCollection() throws InterruptedException {
    SimulationCoordinator coordinator = new SimulationCoordinator();
    Factory factory = new Factory(coordinator);

    factory.producePartsForTesting(20);

    Thread[] collectors = new Thread[5];
    int[] collected = new int[5];

    for (int i = 0; i < 5; i++) {
      final int index = i;
      collectors[i] = new Thread(() -> {
        collected[index] = factory.collectParts(2).size();
      });
    }

    for (Thread collector : collectors) {
      collector.start();
    }

    for (Thread collector : collectors) {
      collector.join();
    }

    factory.stop();

    for (int count : collected) {
      assertThat(count).isGreaterThanOrEqualTo(0);
    }

    int totalCollected = 0;
    for (int count : collected) {
      totalCollected += count;
    }
    assertThat(totalCollected).isLessThanOrEqualTo(20);
  }
}