package com.innowise.skynet.service;

import com.innowise.skynet.model.RobotPart;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Factory that produces robot parts in a separate thread during the SkyNet simulation.
 * <p>
 * The factory operates on a day/night cycle, producing exactly 10 random robot parts
 * per day. "Parts are distributed to factions using a fair distribution algorithm"
 * during the night phase.
 * </p>
 * <p>
 * Thread safety is ensured through the use of locks and atomic operations for
 * fair distribution among competing factions.
 * </p>
 */
public class Factory implements Runnable {
  /** Logger for this class. */
  private static final Logger log = LoggerFactory.getLogger(Factory.class);
  
  /** Number of parts produced per day. */
  private static final int PARTS_PER_DAY = 10;
  
  /** List of available parts for collection. */
  private final List<RobotPart> availableParts;
  
  /** Random number generator for part type selection. */
  private final Random random;
  
  /** Lock for thread-safe access to available parts. */
  private final Lock lock;
  
  /** Simulation coordinator for synchronization. */
  private final SimulationCoordinator coordinator;
  
  
  /** Map to track how many parts each faction has collected for fair distribution. */
  private final Map<Integer, AtomicInteger> factionCollectionCount;
  
  /** Number of factions in the simulation. */
  private static final int TOTAL_FACTIONS = 2;
  
  /** Flag indicating if the factory is running. */
  private volatile boolean running;


  /**
   * Creates a new Factory instance.
   *
   * @param coordinator simulation coordinator for synchronization
   */
  public Factory(SimulationCoordinator coordinator) {
    this.availableParts = new ArrayList<>();
    this.random = new Random();
    this.lock = new ReentrantLock();
    this.coordinator = coordinator;
    this.factionCollectionCount = new ConcurrentHashMap<>();
    this.running = true;
    
    for (int i = 0; i < TOTAL_FACTIONS; i++) {
      factionCollectionCount.put(i, new AtomicInteger(0));
    }
  }

  /**
   * Main execution method for the factory thread.
   * Follows the day/night cycle for the entire simulation duration.
   * 
   * @throws RuntimeException if the thread is interrupted
   */
  @Override
  public void run() {
    try {
      while (coordinator.isSimulationRunning()) {
        produceParts();
        coordinator.waitForDayEnd();
        coordinator.waitForNightEnd();
        coordinator.nextDay();
      }
      log.info("Simulation completed! Total days: {}", coordinator.getCurrentDay());
    } catch (InterruptedException e) {
      Thread.currentThread().interrupt();
    }
  }

  /**
   * Produces exactly 10 random robot parts for the current day.
   * This method is called during the day phase of each simulation cycle.
   */
  private void produceParts() {
    producePartsForTesting(PARTS_PER_DAY);
  }

  /**
   * Public method for testing - produces specified number of parts.
   * Generates random robot parts and adds them to the available parts list.
   * 
   * @param count number of parts to produce
   */
  public void producePartsForTesting(int count) {
    lock.lock();
    try {
      for (int i = 0; i < count; i++) {
        RobotPart randomPart = RobotPart.values()[random.nextInt(RobotPart.values().length)];
        availableParts.add(randomPart);
      }
    } finally {
      lock.unlock();
    }
  }

  /**
   * Allows a faction to collect parts from the factory (for testing).
   * Parts are distributed on first-come-first-served basis.
   *
   * @param maxParts maximum number of parts to collect
   * @return list of collected parts
   */
  public List<RobotPart> collectParts(int maxParts) {
    return collectParts(maxParts, 0);
  }

  /**
   * Allows a faction to collect parts from the factory with fair distribution.
   * Uses round-robin and balanced allocation to ensure both factions get equal opportunities.
   *
   * @param maxParts maximum number of parts to collect
   * @param factionId faction ID for fair distribution tracking
   * @return list of collected parts
   */
  public List<RobotPart> collectParts(int maxParts, int factionId) {
    lock.lock();
    try {
      List<RobotPart> collectedParts = new ArrayList<>();
      
      int availablePartsCount = availableParts.size();
      int fairShare = calculateFairShare(factionId, maxParts, availablePartsCount);

      int partsToTake = Math.min(fairShare, Math.min(maxParts, availablePartsCount));

      for (int i = 0; i < partsToTake; i++) {
        if (!availableParts.isEmpty()) {
          collectedParts.add(availableParts.remove(0));
        }
      }
      
      factionCollectionCount.get(factionId).addAndGet(collectedParts.size());
      
      return collectedParts;
    } finally {
      lock.unlock();
    }
  }
  
  /**
   * Calculates fair share of parts for a faction based on current distribution balance.
   * Ensures that no faction gets significantly more parts than others over time.
   *
   * @param factionId faction requesting parts
   * @param maxParts maximum parts the faction wants to collect
   * @param availablePartsCount total parts currently available
   * @return number of parts this faction should be allowed to collect
   */
  private int calculateFairShare(int factionId, int maxParts, int availablePartsCount) {
    if (availablePartsCount == 0) {
      return 0;
    }
    
    int myCount = factionCollectionCount.get(factionId).get();
    int otherFactionCount = 0;
    
    for (Map.Entry<Integer, AtomicInteger> entry : factionCollectionCount.entrySet()) {
      if (!entry.getKey().equals(factionId)) {
        otherFactionCount += entry.getValue().get();
      }
    }
    
    int imbalance = myCount - (otherFactionCount / Math.max(1, TOTAL_FACTIONS - 1));
    
    if (imbalance > 2) {
      return Math.min(1, Math.min(maxParts, availablePartsCount));
    }
    
    if (imbalance < -2) {
      return Math.min(maxParts, availablePartsCount);
    }
    
    int fairLimit = Math.max(1, availablePartsCount / TOTAL_FACTIONS + 1);
    return Math.min(maxParts, Math.min(fairLimit, availablePartsCount));
  }

  /**
   * Gets the current number of available parts in the factory.
   *
   * @return number of available parts
   */
  public int getAvailablePartsCount() {
    lock.lock();
    try {
      return availableParts.size();
    } finally {
      lock.unlock();
    }
  }

  /**
   * Stops the factory production.
   */
  public void stop() {
    running = false;
  }

  /**
   * Gets the current simulation day (thread-safe).
   *
   * @return current day number
   */
  public int getCurrentDay() {
    return coordinator.getCurrentDay();
  }

  /**
   * Checks if the factory is still running.
   *
   * @return true if factory is running, false otherwise
   */
  public boolean isRunning() {
    return running;
  }
  
  /**
   * Gets the total number of parts collected by a specific faction.
   * Used for monitoring fair distribution.
   *
   * @param factionId faction ID to check
   * @return total parts collected by the faction
   */
  public int getPartsCollectedByFaction(int factionId) {
    return factionCollectionCount.getOrDefault(factionId, new AtomicInteger(0)).get();
  }
}
