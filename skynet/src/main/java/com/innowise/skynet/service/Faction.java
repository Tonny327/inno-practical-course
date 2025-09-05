package com.innowise.skynet.service;

import com.innowise.skynet.model.RobotPart;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;

/**
 * Faction that collects robot parts and builds robots.
 * Each faction can carry no more than 5 parts per night.
 */
public class Faction implements Runnable {
  private static final int MAX_PARTS_PER_NIGHT = 5;

  private final String name;
  private final Factory factory;
  private final Map<RobotPart, Integer> inventory;
  private int completedRobots;
  private volatile boolean running;

  /**
   * Creates a new Faction with the given name and factory reference.
   *
   * @param name the name of the faction
   * @param factory the factory to collect parts from
   */
  public Faction(String name, Factory factory) {
    this.name = name;
    this.factory = factory;
    this.inventory = new EnumMap<>(RobotPart.class);
    this.completedRobots = 0;
    this.running = true;

    for (RobotPart part : RobotPart.values()) {
      inventory.put(part, 0);
    }
  }

  @Override
  public void run() {
    try {
      while (running && factory.getCurrentDay() < 100) {
        Thread.sleep(120);

        if (factory.getCurrentDay() < 100) {
          collectPartsFromFactory();
          buildRobots();
        }
      }
    } catch (InterruptedException e) {
      Thread.currentThread().interrupt();
    }
  }

  /**
   * Collects parts from the factory during night time.
   */
  private void collectPartsFromFactory() {
    List<RobotPart> collectedParts = factory.collectParts(MAX_PARTS_PER_NIGHT);

    for (RobotPart part : collectedParts) {
      inventory.put(part, inventory.get(part) + 1);
    }
  }

  /**
   * Builds complete robots using available parts.
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
