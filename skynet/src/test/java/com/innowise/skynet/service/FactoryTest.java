package com.innowise.skynet.service;

import com.innowise.skynet.model.RobotPart;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Timeout;

import java.util.List;


import static org.assertj.core.api.Assertions.assertThat;


/**
 * Unit tests for Factory class.
 * <p>
 * Tests verify the functionality of the robot parts factory including:
 * <ul>
 * <li>Parts production and availability</li>
 * <li>Parts collection mechanisms and limits</li>
 * <li>Factory lifecycle management (start/stop)</li>
 * <li>Fair distribution algorithm between factions</li>
 * <li>Prevention of monopolization by single faction</li>
 * </ul>
 * </p>
 */
class FactoryTest {

  /** Factory instance for testing. */
  private Factory factory;

  /**
   * Sets up the test environment before each test.
   */
  @BeforeEach
  void setUp() {
    SimulationCoordinator coordinator = new SimulationCoordinator();
    factory = new Factory(coordinator);
  }

  /**
   * Tests that factory initializes with zero parts and correct initial state.
   */
  @Test
  void shouldInitializeWithZeroParts() {
    assertThat(factory.getAvailablePartsCount()).isEqualTo(0);
    assertThat(factory.getCurrentDay()).isEqualTo(0);
    assertThat(factory.isRunning()).isTrue();
  }

  /**
   * Tests that factions can collect parts from available inventory.
   */
  @Test
  void shouldCollectPartsFromAvailableParts() {
    factory.producePartsForTesting(5);

    List<RobotPart> collectedParts = factory.collectParts(3);

    assertThat(collectedParts).isNotEmpty();
    assertThat(collectedParts.size()).isLessThanOrEqualTo(3);
    assertThat(factory.getAvailablePartsCount()).isEqualTo(2); // 5 - 3 = 2
  }

  /**
   * Tests that collection respects the maximum parts requested limit.
   */
  @Test
  void shouldNotCollectMorePartsThanRequested() {
    factory.producePartsForTesting(5);

    List<RobotPart> collectedParts = factory.collectParts(2);

    assertThat(collectedParts.size()).isEqualTo(2);
    assertThat(factory.getAvailablePartsCount()).isEqualTo(3); // 5 - 2 = 3
  }

  /**
   * Tests that collection cannot exceed available parts count.
   */
  @Test
  void shouldNotCollectMorePartsThanAvailable() {

    List<RobotPart> collectedParts = factory.collectParts(5);

    assertThat(collectedParts).isEmpty();
  }

  /**
   * Tests that factory stops correctly when requested.
   */
  @Test
  @Timeout(5)
  void shouldStopWhenRequested() {
    assertThat(factory.isRunning()).isTrue();
    
    factory.stop();
    
    assertThat(factory.isRunning()).isFalse();
  }

  /**
   * Tests that factory can produce parts correctly.
   */
  @Test
  @Timeout(10)
  void shouldProducePartsOverTime() {
    assertThat(factory.getAvailablePartsCount()).isEqualTo(0);
    
    factory.producePartsForTesting(10);
    assertThat(factory.getAvailablePartsCount()).isEqualTo(10);
    
    factory.producePartsForTesting(5);
    assertThat(factory.getAvailablePartsCount()).isEqualTo(15);
  }
  
  /**
   * Tests that parts are distributed fairly between factions.
   */
  @Test
  void shouldDistributePartsFairlyBetweenFactions() {
    factory.producePartsForTesting(20);
    
    for (int round = 0; round < 4; round++) {
      factory.collectParts(5, 0); // World faction
      factory.collectParts(5, 1); // Wednesday faction
    }
    
    int worldCollected = factory.getPartsCollectedByFaction(0);
    int wednesdayCollected = factory.getPartsCollectedByFaction(1);
    
    int difference = Math.abs(worldCollected - wednesdayCollected);
    assertThat(difference).isLessThanOrEqualTo(3);
    
    assertThat(worldCollected).isGreaterThan(0);
    assertThat(wednesdayCollected).isGreaterThan(0);
  }
  
  /**
   * Tests that fair distribution prevents one faction from taking everything.
   */
  @Test
  void shouldPreventOneFactionFromTakingAllParts() {
    factory.producePartsForTesting(10);
    
    List<RobotPart> firstCollection = factory.collectParts(10, 0);
    
    List<RobotPart> secondCollection = factory.collectParts(10, 1);
    
    assertThat(firstCollection.size()).isLessThan(10);
    
    assertThat(secondCollection.size()).isGreaterThan(0);
  }
}
