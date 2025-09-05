package com.innowise.skynet.service;

import com.innowise.skynet.model.RobotPart;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * Factory that produces robot parts in a separate thread.
 * Produces no more than 10 parts per day with random part types.
 */
public class Factory implements Runnable {
  private static final int MAX_PARTS_PER_DAY = 10;
  private final List<RobotPart> availableParts;
  private final Random random;
  private final Lock lock;
  private volatile boolean running;
  private int currentDay;

  /**
   * Creates a new Factory instance.
   */
  public Factory() {
    this.availableParts = new ArrayList<>();
    this.random = new Random();
    this.lock = new ReentrantLock();
    this.running = true;
    this.currentDay = 0;
  }

  @Override
  public void run() {
    try {
      while (running && currentDay < 100) {
        produceParts();
        currentDay++;
        Thread.sleep(100);
      }
    } catch (InterruptedException e) {
      Thread.currentThread().interrupt();
    }
  }

  /**
   * Produces random robot parts for the current day.
   */
  private void produceParts() {
    lock.lock();
    try {
      int partsToMake = random.nextInt(MAX_PARTS_PER_DAY) + 1;

      for (int i = 0; i < partsToMake; i++) {
        RobotPart randomPart = RobotPart.values()[random.nextInt(RobotPart.values().length)];
        availableParts.add(randomPart);
      }
    } finally {
      lock.unlock();
    }
  }

  /**
   * Allows a faction to collect parts from the factory.
   *
   * @param maxParts maximum number of parts to collect
   * @return list of collected parts
   */
  public List<RobotPart> collectParts(int maxParts) {
    lock.lock();
    try {
      List<RobotPart> collectedParts = new ArrayList<>();
      int partsToTake = Math.min(maxParts, availableParts.size());

      for (int i = 0; i < partsToTake; i++) {
        if (!availableParts.isEmpty()) {
          collectedParts.add(availableParts.remove(0));
        }
      }

      return collectedParts;
    } finally {
      lock.unlock();
    }
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
   * Gets the current simulation day.
   *
   * @return current day number
   */
  public int getCurrentDay() {
    return currentDay;
  }

  /**
   * Checks if the factory is still running.
   *
   * @return true if factory is running, false otherwise
   */
  public boolean isRunning() {
    return running;
  }
}
