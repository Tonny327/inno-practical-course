package com.innowise.skynet.service;

import com.innowise.skynet.model.SimulationResult;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Timeout;

import java.util.concurrent.TimeUnit;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Integration tests for SimulationService.
 */
class SimulationServiceTest {

  private SimulationService simulationService;

  @BeforeEach
  void setUp() {
    simulationService = new SimulationService();
  }

  @Test
  void shouldInitializeSimulationCorrectly() {
    simulationService.initializeSimulation();

    assertThat(simulationService.getFactory()).isNotNull();
    assertThat(simulationService.getWorldFaction()).isNotNull();
    assertThat(simulationService.getWednesdayFaction()).isNotNull();

    assertThat(simulationService.getWorldFaction().getName()).isEqualTo("World");
    assertThat(simulationService.getWednesdayFaction().getName()).isEqualTo("Wednesday");
  }

  @Test
  @Timeout(value = 30, unit = TimeUnit.SECONDS)
  void shouldRunFullSimulation() {
    SimulationResult result = simulationService.runSimulation();

    assertThat(result).isNotNull();
    assertThat(result.getWinner()).isIn("World", "Wednesday", "Tie");
    assertThat(result.getWorldRobots()).isGreaterThanOrEqualTo(0);
    assertThat(result.getWednesdayRobots()).isGreaterThanOrEqualTo(0);
    assertThat(result.getWorldInventory()).isNotNull();
    assertThat(result.getWednesdayInventory()).isNotNull();
  }

  @Test
  @Timeout(value = 30, unit = TimeUnit.SECONDS)
  void shouldProduceReasonableResults() {
    SimulationResult result = simulationService.runSimulation();

    int totalRobots = result.getWorldRobots() + result.getWednesdayRobots();
    assertThat(totalRobots).isGreaterThan(0);

    if (result.getWorldRobots() > result.getWednesdayRobots()) {
      assertThat(result.getWinner()).isEqualTo("World");
    } else if (result.getWednesdayRobots() > result.getWorldRobots()) {
      assertThat(result.getWinner()).isEqualTo("Wednesday");
    } else {
      assertThat(result.getWinner()).isEqualTo("Tie");
    }
  }

  @Test
  @Timeout(value = 60, unit = TimeUnit.SECONDS)
  void shouldBeConsistentAcrossMultipleRuns() {
    for (int i = 0; i < 3; i++) {
      simulationService = new SimulationService();
      SimulationResult result = simulationService.runSimulation();

      assertThat(result.getWinner()).isIn("World", "Wednesday", "Tie");
      assertThat(result.getWorldRobots()).isGreaterThanOrEqualTo(0);
      assertThat(result.getWednesdayRobots()).isGreaterThanOrEqualTo(0);
    }
  }
}
