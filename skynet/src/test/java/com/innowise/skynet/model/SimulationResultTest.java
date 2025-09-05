package com.innowise.skynet.model;

import org.junit.jupiter.api.Test;

import java.util.EnumMap;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Unit tests for SimulationResult class.
 */
class SimulationResultTest {

  @Test
  void shouldCreateResultCorrectly() {
    Map<RobotPart, Integer> worldInventory = new EnumMap<>(RobotPart.class);
    Map<RobotPart, Integer> wednesdayInventory = new EnumMap<>(RobotPart.class);

    worldInventory.put(RobotPart.HEAD, 2);
    wednesdayInventory.put(RobotPart.TORSO, 1);

    SimulationResult result = new SimulationResult("World", 5, 3,
        worldInventory, wednesdayInventory);

    assertThat(result.getWinner()).isEqualTo("World");
    assertThat(result.getWorldRobots()).isEqualTo(5);
    assertThat(result.getWednesdayRobots()).isEqualTo(3);
    assertThat(result.getWorldInventory()).isEqualTo(worldInventory);
    assertThat(result.getWednesdayInventory()).isEqualTo(wednesdayInventory);
  }
}