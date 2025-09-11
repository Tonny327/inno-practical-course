package com.innowise.skynet.model;

import java.util.Map;
import lombok.Getter;
import lombok.AllArgsConstructor;

/**
 * Result of the robot war simulation containing winner information and statistics.
 * <p>
 * This class encapsulates all data about the results of a 100-day simulation, including the number
 * of robots created by each faction and remaining part inventories.
 * </p>
 */
@Getter
@AllArgsConstructor
public class SimulationResult {

  /**
   * Name of the winning faction ("World", "Wednesday" or "Tie" in case of a draw).
   */
  private final String winner;

  /**
   * Number of robots created by the "World" faction.
   */
  private final int worldRobots;

  /**
   * Number of robots created by the "Wednesday" faction.
   */
  private final int wednesdayRobots;

  /**
   * Robot parts inventory of the "World" faction at the end of simulation. Map contains robot part
   * type as key and quantity as value.
   */
  private final Map<RobotPart, Integer> worldInventory;

  /**
   * Robot parts inventory of the "Wednesday" faction at the end of simulation. Map contains robot
   * part type as key and quantity as value.
   */
  private final Map<RobotPart, Integer> wednesdayInventory;

}
