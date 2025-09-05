package com.innowise.skynet.model;

import java.util.Map;
import lombok.*;

/**
 * Result of the robot war simulation containing winner and statistics.
 */
@Getter
@AllArgsConstructor
public class SimulationResult {
  private final String winner;
  private final int worldRobots;
  private final int wednesdayRobots;
  private final Map<RobotPart, Integer> worldInventory;
  private final Map<RobotPart, Integer> wednesdayInventory;


}
