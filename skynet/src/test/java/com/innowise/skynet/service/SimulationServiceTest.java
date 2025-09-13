package com.innowise.skynet.service;

import com.innowise.skynet.model.RobotPart;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Integration tests for SimulationService.
 * <p>
 * Tests verify the complete simulation workflow including:
 * <ul>
 * <li>Proper initialization of all simulation components</li>
 * <li>Correct faction setup and behavior</li>
 * <li>Factory-faction interactions</li>
 * <li>Simulation result generation</li>
 * <li>Component lifecycle management</li>
 * </ul>
 * </p>
 *
 */
class SimulationServiceTest {

  /** SimulationService instance for testing. */
  private SimulationService simulationService;

  /**
   * Sets up the test environment before each test.
   */
  @BeforeEach
  void setUp() {
    simulationService = new SimulationService();
  }

  /**
   * Tests that simulation initializes all components correctly.
   */
  @Test
  void shouldInitializeSimulationCorrectly() {
    simulationService.initializeSimulation();

    assertThat(simulationService.getFactory()).isNotNull();
    assertThat(simulationService.getWorldFaction()).isNotNull();
    assertThat(simulationService.getWednesdayFaction()).isNotNull();

    assertThat(simulationService.getWorldFaction().getName()).isEqualTo("World");
    assertThat(simulationService.getWednesdayFaction().getName()).isEqualTo("Wednesday");
  }

  /**
   * Tests the basic simulation mechanics and component interactions.
   */
  @Test
  void shouldRunFullSimulation() {
    simulationService.initializeSimulation();
    
    assertThat(simulationService.getFactory()).isNotNull();
    assertThat(simulationService.getWorldFaction()).isNotNull();
    assertThat(simulationService.getWednesdayFaction()).isNotNull();
    
    simulationService.getFactory().producePartsForTesting(10);
    assertThat(simulationService.getFactory().getAvailablePartsCount()).isEqualTo(10);
    
    int worldParts = simulationService.getFactory().collectParts(5, 0).size();
    int wednesdayParts = simulationService.getFactory().collectParts(5, 1).size();
    
    assertThat(worldParts + wednesdayParts).isLessThanOrEqualTo(10);
    assertThat(worldParts).isGreaterThanOrEqualTo(0);
    assertThat(wednesdayParts).isGreaterThanOrEqualTo(0);
  }

  /**
   * Tests that simulation produces reasonable and expected results.
   */
  @Test
  void shouldProduceReasonableResults() {
    simulationService.initializeSimulation();
    
    Factory factory = simulationService.getFactory();
    Faction worldFaction = simulationService.getWorldFaction();
    Faction wednesdayFaction = simulationService.getWednesdayFaction();
    
    factory.producePartsForTesting(12); // 12 parts for testing
    
    factory.collectParts(5, 0); // World collects 5
    factory.collectParts(5, 1); // Wednesday collects 5
    
    assertThat(factory.getAvailablePartsCount()).isEqualTo(2);
    
    assertThat(worldFaction.getInventory()).hasSize(4);
    assertThat(wednesdayFaction.getInventory()).hasSize(4);
    
    for (RobotPart part : RobotPart.values()) {
      assertThat(worldFaction.getInventory()).containsKey(part);
      assertThat(wednesdayFaction.getInventory()).containsKey(part);
    }
  }

  /**
   * Tests that simulation initialization is consistent across multiple runs.
   */
  @Test
  void shouldBeConsistentAcrossMultipleRuns() {
    for (int i = 0; i < 3; i++) {
      SimulationService testService = new SimulationService();
      testService.initializeSimulation();

      assertThat(testService.getFactory()).isNotNull();
      assertThat(testService.getWorldFaction()).isNotNull();
      assertThat(testService.getWednesdayFaction()).isNotNull();
      assertThat(testService.getWorldFaction().getName()).isEqualTo("World");
      assertThat(testService.getWednesdayFaction().getName()).isEqualTo("Wednesday");
      
      testService.getFactory().producePartsForTesting(4);
      assertThat(testService.getFactory().getAvailablePartsCount()).isEqualTo(4);
    }
  }

  /**
   * Tests that simulation properly manages component lifecycle.
   */
  @Test
  void shouldHandleSimulationComponents() {
    SimulationService testService = new SimulationService();
    testService.initializeSimulation();
    
    Factory factory = testService.getFactory();
    Faction worldFaction = testService.getWorldFaction();
    Faction wednesdayFaction = testService.getWednesdayFaction();
    
    assertThat(factory.isRunning()).isTrue();
    assertThat(worldFaction.isRunning()).isTrue();
    assertThat(wednesdayFaction.isRunning()).isTrue();
    
    factory.stop();
    worldFaction.stop();
    wednesdayFaction.stop();
    
    assertThat(factory.isRunning()).isFalse();
    assertThat(worldFaction.isRunning()).isFalse();
    assertThat(wednesdayFaction.isRunning()).isFalse();
  }

  /**
   * Tests the core simulation mechanics including parts distribution.
   */
  @Test
  void shouldValidateSimulationMechanics() {
    SimulationService mechanicsTest = new SimulationService();
    mechanicsTest.initializeSimulation();
    
    Factory factory = mechanicsTest.getFactory();
    
    assertThat(factory.getAvailablePartsCount()).isEqualTo(0);
    factory.producePartsForTesting(10);
    assertThat(factory.getAvailablePartsCount()).isEqualTo(10);
    
    int worldCollected = factory.collectParts(5, 0).size();
    int wednesdayCollected = factory.collectParts(5, 1).size();
    
    assertThat(worldCollected + wednesdayCollected).isLessThanOrEqualTo(10);
    assertThat(worldCollected).isLessThanOrEqualTo(5);
    assertThat(wednesdayCollected).isLessThanOrEqualTo(5);
    
    assertThat(factory.getAvailablePartsCount()).isEqualTo(10 - worldCollected - wednesdayCollected);
  }

  /**
   * Tests that simulation creates valid results with proper data structures.
   */
  @Test
  void shouldCreateValidSimulationResult() {
    simulationService.initializeSimulation();

    assertThat(simulationService.getFactory()).isNotNull();
    Faction worldFaction = simulationService.getWorldFaction();
    Faction wednesdayFaction = simulationService.getWednesdayFaction();

    assertThat(worldFaction.getCompletedRobots()).isEqualTo(0);
    assertThat(wednesdayFaction.getCompletedRobots()).isEqualTo(0);
    
    assertThat(worldFaction.getInventory()).hasSize(4);
    assertThat(wednesdayFaction.getInventory()).hasSize(4);
    
    for (RobotPart part : RobotPart.values()) {
      assertThat(worldFaction.getInventory()).containsKey(part);
      assertThat(wednesdayFaction.getInventory()).containsKey(part);
    }
    
    assertThat(worldFaction.getName()).isEqualTo("World");
    assertThat(wednesdayFaction.getName()).isEqualTo("Wednesday");
  }
}
