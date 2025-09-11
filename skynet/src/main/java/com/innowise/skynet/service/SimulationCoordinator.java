package com.innowise.skynet.service;

import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * Simulation coordinator that manages day/night cycles and thread synchronization.
 * <p>
 * Ensures proper coordination between factory and factions using cyclic barriers.
 * Controls the simulation lifecycle for exactly 100 days, managing the transitions
 * between day and night phases.
 * </p>
 * <p>
 * The coordinator uses two barriers:
 * <ul>
 * <li>Day End Barrier: Synchronizes after parts production</li>
 * <li>Night End Barrier: Synchronizes after parts collection and robot building</li>
 * </ul>
 * </p>
 */
public class SimulationCoordinator {
    /** Total number of days in the simulation. */
    private static final int TOTAL_DAYS = 100;
    
    /** Number of participants (Factory + 2 Factions). */
    private static final int PARTICIPANTS_COUNT = 3;
    
    /** Current day counter (atomic for thread safety). */
    private final AtomicInteger currentDay;
    
    /** Flag indicating if simulation is running (atomic for thread safety). */
    private final AtomicBoolean simulationRunning;
    
    /** Barrier for synchronizing end of day phase. */
    private final CyclicBarrier dayEndBarrier;
    
    /** Barrier for synchronizing end of night phase. */
    private final CyclicBarrier nightEndBarrier;
    
    /**
     * Creates a new simulation coordinator.
     */
    public SimulationCoordinator() {
        this.currentDay = new AtomicInteger(0);
        this.simulationRunning = new AtomicBoolean(true);
        this.dayEndBarrier = new CyclicBarrier(PARTICIPANTS_COUNT);
        this.nightEndBarrier = new CyclicBarrier(PARTICIPANTS_COUNT);
    }
    
    /**
     * Waits for day end. Called by all participants after parts production.
     * Uses a cyclic barrier to ensure all threads reach this point before proceeding.
     * 
     * @throws InterruptedException if the thread is interrupted while waiting
     */
    public void waitForDayEnd() throws InterruptedException {
        try {
            dayEndBarrier.await();
        } catch (Exception e) {
            Thread.currentThread().interrupt();
            throw new InterruptedException("Day synchronization interrupted");
        }
    }
    
    /**
     * Waits for night end. Called by all participants after parts collection and robot building.
     * Uses a cyclic barrier to ensure all threads complete night activities before proceeding.
     * 
     * @throws InterruptedException if the thread is interrupted while waiting
     */
    public void waitForNightEnd() throws InterruptedException {
        try {
            nightEndBarrier.await();
        } catch (Exception e) {
            Thread.currentThread().interrupt();
            throw new InterruptedException("Night synchronization interrupted");
        }
    }
    
    /**
     * Advances to the next day. Called only by factory after night phase completion.
     * Increments the day counter and stops simulation if 100 days are reached.
     */
    public void nextDay() {
        int day = currentDay.incrementAndGet();
        if (day >= TOTAL_DAYS) {
            simulationRunning.set(false);
        }
    }
    
    /**
     * Gets current day (thread-safe).
     * 
     * @return current day number (0-based)
     */
    public int getCurrentDay() {
        return currentDay.get();
    }
    
    /**
     * Checks if simulation is still running.
     * 
     * @return true if simulation should continue, false otherwise
     */
    public boolean isSimulationRunning() {
        return simulationRunning.get() && currentDay.get() < TOTAL_DAYS;
    }
    
    /**
     * Stops simulation forcefully.
     * Sets the running flag to false, causing all participants to exit.
     */
    public void stopSimulation() {
        simulationRunning.set(false);
    }
}
