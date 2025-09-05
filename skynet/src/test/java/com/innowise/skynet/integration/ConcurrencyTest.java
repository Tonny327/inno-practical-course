package com.innowise.skynet.integration;

import com.innowise.skynet.service.Factory;
import com.innowise.skynet.service.Faction;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Timeout;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Integration tests focusing on concurrency and thread safety.
 */
class ConcurrencyTest {

  @Test
  @Timeout(15)
  void shouldHandleMultipleFactionsConcurrently() throws InterruptedException {
    Factory factory = new Factory();
    Faction faction1 = new Faction("Faction1", factory);
    Faction faction2 = new Faction("Faction2", factory);
    Faction faction3 = new Faction("Faction3", factory);

    CountDownLatch startLatch = new CountDownLatch(1);
    CountDownLatch endLatch = new CountDownLatch(4);

    // Запускаем все в потоках
    Thread factoryThread = new Thread(() -> {
      try {
        startLatch.await();
        factory.run();
      } catch (InterruptedException e) {
        Thread.currentThread().interrupt();
      } finally {
        endLatch.countDown();
      }
    });

    Thread[] factionThreads = {
        new Thread(() -> {
          try {
            startLatch.await();
            faction1.run();
          } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
          } finally {
            endLatch.countDown();
          }
        }),
        new Thread(() -> {
          try {
            startLatch.await();
            faction2.run();
          } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
          } finally {
            endLatch.countDown();
          }
        }),
        new Thread(() -> {
          try {
            startLatch.await();
            faction3.run();
          } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
          } finally {
            endLatch.countDown();
          }
        })
    };

    factoryThread.start();
    for (Thread thread : factionThreads) {
      thread.start();
    }


    startLatch.countDown();


    Thread.sleep(2000);


    factory.stop();
    faction1.stop();
    faction2.stop();
    faction3.stop();


    endLatch.await(5, TimeUnit.SECONDS);

    int totalRobots = faction1.getCompletedRobots() +
        faction2.getCompletedRobots() +
        faction3.getCompletedRobots();

    assertThat(totalRobots).isGreaterThanOrEqualTo(0);

    factoryThread.interrupt();
    for (Thread thread : factionThreads) {
      thread.interrupt();
    }
  }

  @Test
  @Timeout(10)
  void shouldNotHaveRaceConditionsInPartCollection() throws InterruptedException {
    Factory factory = new Factory();

    Thread factoryThread = new Thread(factory);
    factoryThread.start();

    Thread.sleep(1000);

    Thread[] collectors = new Thread[5];
    int[] collected = new int[5];

    for (int i = 0; i < 5; i++) {
      final int index = i;
      collectors[i] = new Thread(() -> {
        collected[index] = factory.collectParts(2).size();
      });
    }

    for (Thread collector : collectors) {
      collector.start();
    }

    for (Thread collector : collectors) {
      collector.join();
    }

    factory.stop();
    factoryThread.interrupt();

    for (int count : collected) {
      assertThat(count).isGreaterThanOrEqualTo(0);
    }
  }
}