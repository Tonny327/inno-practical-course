package com.innowise.skynet.service;

import com.innowise.skynet.model.SimulationResult;

import lombok.*;

/**
 * Service for running the robot war simulation between factions.
 * Manages the 100-day simulation where factions compete to build robot armies.
 */
@Getter
public class SimulationService {

  private Factory factory;
  private Faction worldFaction;
  private Faction wednesdayFaction;

  /**
   * Initializes the simulation with factory and factions.
   */
  public void initializeSimulation() {
    factory = new Factory();
    worldFaction = new Faction("World", factory);
    wednesdayFaction = new Faction("Wednesday", factory);
  }

  /**
   * Runs the complete simulation for 100 days.
   *
   * @return simulation result containing winner and statistics
   */
  public SimulationResult runSimulation() {
    if (factory == null) {
      initializeSimulation();
    }

    Thread factoryThread = new Thread(factory);
    Thread worldThread = new Thread(worldFaction);
    Thread wednesdayThread = new Thread(wednesdayFaction);

    factoryThread.start();
    worldThread.start();
    wednesdayThread.start();

    try {
      factoryThread.join();

      worldFaction.stop();
      wednesdayFaction.stop();

      worldThread.join(1000);
      wednesdayThread.join(1000);

    } catch (InterruptedException e) {
      Thread.currentThread().interrupt();
      throw new RuntimeException("Simulation was interrupted", e);
    }

    return createSimulationResult();
  }

  /**
   * Creates simulation result based on faction performance.
   *
   * @return simulation result with winner and statistics
   */
  private SimulationResult createSimulationResult() {
    int worldRobots = worldFaction.getCompletedRobots();
    int wednesdayRobots = wednesdayFaction.getCompletedRobots();

    String winner;
    if (worldRobots > wednesdayRobots) {
      winner = "World";
    } else if (wednesdayRobots > worldRobots) {
      winner = "Wednesday";
    } else {
      winner = "Tie";
    }

    return new SimulationResult(winner, worldRobots, wednesdayRobots,
        worldFaction.getInventory(), wednesdayFaction.getInventory());
  }

}