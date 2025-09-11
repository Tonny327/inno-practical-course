package com.innowise.skynet.service;

import com.innowise.skynet.model.SimulationResult;

import lombok.Getter;
import lombok.Setter;

/**
 * Service for running the robot war simulation between factions.
 * <p>
 * Manages the complete 100-day simulation where two factions ("World" and "Wednesday") 
 * compete to build the largest robot army. The service coordinates the factory,
 * factions, and simulation lifecycle.
 * </p>
 * <p>
 * The simulation follows these phases:
 * <ul>
 * <li>Initialization: Creates coordinator, factory, and factions</li>
 * <li>Execution: Runs all components in separate threads</li>
 * <li>Completion: Determines winner and collects final statistics</li>
 * </ul>
 * </p>
 */
@Getter
@Setter
public class SimulationService {

  /** Simulation coordinator for thread synchronization. */
  private SimulationCoordinator coordinator;
  
  /** Factory that produces robot parts. */
  private Factory factory;
  
  /** World faction competing in the simulation. */
  private Faction worldFaction;
  
  /** Wednesday faction competing in the simulation. */
  private Faction wednesdayFaction;

  /**
   * Initializes the simulation with coordinator, factory and factions.
   * Creates all necessary components and sets up their relationships.
   * Must be called before running the simulation.
   */
  public void initializeSimulation() {
    coordinator = new SimulationCoordinator();
    factory = new Factory(coordinator);
    worldFaction = new Faction("World", 0, factory, coordinator);
    wednesdayFaction = new Faction("Wednesday", 1, factory, coordinator);
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

      worldThread.join();
      wednesdayThread.join();

    } catch (InterruptedException e) {
      Thread.currentThread().interrupt();
      coordinator.stopSimulation();
      throw new RuntimeException("Simulation was interrupted", e);
    }

    return createSimulationResult();
  }

  /**
   * Creates simulation result based on faction performance.
   * Determines the winner by comparing robot counts and collects
   * final inventories from both factions.
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