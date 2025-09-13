package com.innowise.skynet.service;

import com.innowise.skynet.model.RobotPart;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Faction that collects robot parts and builds robots in the SkyNet simulation.
 * <p>
 * Each faction operates in a separate thread and follows a day/night cycle:
 * <ul>
 * <li>Day: waiting for factory to produce parts</li>
 * <li>Night: collecting parts (up to 5 per night) and building robots</li>
 * </ul>
 * </p>
 * <p>
 * A faction can collect no more than 5 parts per night. To create a robot,
 * one part of each type is required (HEAD, TORSO, HAND, FEET).
 * </p>
 */
public class Faction implements Runnable {

  /**
   * Logger for this class.
   */
  private static final Logger log = LoggerFactory.getLogger(Faction.class);

  /**
   * Maximum number of parts a faction can collect per night.
   */
  private static final int MAX_PARTS_PER_NIGHT = 5;

  /**
   * Name of the faction.
   */
  private final String name;

  /**
   * Unique identifier for fair distribution among factions.
   */
  private final int factionId;

  /**
   * Factory reference for collecting parts.
   */
  private final Factory factory;

  /**
   * Simulation coordinator for synchronization.
   */
  private final SimulationCoordinator coordinator;

  /**
   * Current inventory of robot parts.
   */
  private final Map<RobotPart, Integer> inventory;

  /**
   * Number of completed robots.
   */
  private int completedRobots;

  /**
   * Flag indicating if the faction is running.
   */
  private volatile boolean running;


  /**
   * Creates a new Faction with the given name, factory reference and coordinator.
   *
   * @param name        the name of the faction
   * @param factionId   unique faction ID for fair distribution
   * @param factory     the factory to collect parts from
   * @param coordinator simulation coordinator for synchronization
   */
  public Faction(String name, int factionId, Factory factory, SimulationCoordinator coordinator) {
    this.name = name;
    this.factionId = factionId;
    this.factory = factory;
    this.coordinator = coordinator;
    this.inventory = new EnumMap<>(RobotPart.class);
    this.completedRobots = 0;
    this.running = true;

    for (RobotPart part : RobotPart.values()) {
      inventory.put(part, 0);
    }
  }

  /**
   * Main execution method for the faction thread. Follows the day/night cycle until simulation
   * ends.
   *
   * @throws RuntimeException if the thread is interrupted
   */
  @Override
  public void run() {
    try {
      while (coordinator.isSimulationRunning()) {
        coordinator.waitForDayEnd();

        collectPartsFromFactory();
        buildRobots();

        coordinator.waitForNightEnd();
      }
      log.info("Faction {} finished work. Robots: {}", name, completedRobots);
    } catch (InterruptedException e) {
      Thread.currentThread().interrupt();
    }
  }

  /**
   * Collects parts from the factory during night time with fair distribution. Uses the faction's
   * unique ID to ensure fair access to factory parts.
   */
  private void collectPartsFromFactory() {
    List<RobotPart> collectedParts = factory.collectParts(MAX_PARTS_PER_NIGHT, factionId);

    for (RobotPart part : collectedParts) {
      inventory.put(part, inventory.get(part) + 1);
    }
  }

  /**
   * Builds complete robots using available parts. Determines the maximum number of robots that can
   * be built based on the minimum count of any part type in inventory.
   */
  private void buildRobots() {
    int robotsToBuild = Integer.MAX_VALUE;

    for (RobotPart part : RobotPart.values()) {
      robotsToBuild = Math.min(robotsToBuild, inventory.get(part));
    }

    if (robotsToBuild > 0) {
      for (RobotPart part : RobotPart.values()) {
        inventory.put(part, inventory.get(part) - robotsToBuild);
      }

      completedRobots += robotsToBuild;
    }
  }

  /**
   * Gets the faction name.
   *
   * @return faction name
   */
  public String getName() {
    return name;
  }

  /**
   * Gets the number of completed robots.
   *
   * @return number of completed robots
   */
  public int getCompletedRobots() {
    return completedRobots;
  }

  /**
   * Gets a copy of the current inventory.
   *
   * @return map of robot parts and their quantities
   */
  public Map<RobotPart, Integer> getInventory() {
    return new EnumMap<>(inventory);
  }

  /**
   * Stops the faction from operating.
   */
  public void stop() {
    running = false;
  }

  /**
   * Checks if the faction is still running.
   *
   * @return true if faction is running, false otherwise
   */
  public boolean isRunning() {
    return running;
  }
}
