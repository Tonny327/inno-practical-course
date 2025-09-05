package com.innowise.skynet.service;

import com.innowise.skynet.model.RobotPart;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Timeout;

import java.util.List;
import java.util.concurrent.TimeUnit;

import static org.assertj.core.api.Assertions.assertThat;
import static org.awaitility.Awaitility.await;

/**
 * Unit tests for Factory class.
 */
class FactoryTest {

  private Factory factory;

  @BeforeEach
  void setUp() {
    factory = new Factory();
  }

  @Test
  void shouldInitializeWithZeroParts() {
    assertThat(factory.getAvailablePartsCount()).isEqualTo(0);
    assertThat(factory.getCurrentDay()).isEqualTo(0);
    assertThat(factory.isRunning()).isTrue();
  }

  @Test
  void shouldCollectPartsFromAvailableParts() {
    // Запускаем фабрику в отдельном потоке
    Thread factoryThread = new Thread(factory);
    factoryThread.start();


    await().atMost(2, TimeUnit.SECONDS)
        .until(() -> factory.getAvailablePartsCount() > 0);


    List<RobotPart> collectedParts = factory.collectParts(3);

    assertThat(collectedParts).isNotEmpty();
    assertThat(collectedParts.size()).isLessThanOrEqualTo(3);

    factory.stop();
    factoryThread.interrupt();
  }

  @Test
  void shouldNotCollectMorePartsThanRequested() {

    Thread factoryThread = new Thread(factory);
    factoryThread.start();


    await().atMost(2, TimeUnit.SECONDS)
        .until(() -> factory.getAvailablePartsCount() >= 5);


    List<RobotPart> collectedParts = factory.collectParts(2);

    assertThat(collectedParts.size()).isLessThanOrEqualTo(2);

    factory.stop();
    factoryThread.interrupt();
  }

  @Test
  void shouldNotCollectMorePartsThanAvailable() {

    List<RobotPart> collectedParts = factory.collectParts(5);

    assertThat(collectedParts).isEmpty();
  }

  @Test
  @Timeout(5)
  void shouldStopWhenRequested() {
    Thread factoryThread = new Thread(factory);
    factoryThread.start();


    await().pollDelay(100, TimeUnit.MILLISECONDS)
        .until(() -> true);

    factory.stop();

    await().atMost(2, TimeUnit.SECONDS)
        .until(() -> !factory.isRunning());

    factoryThread.interrupt();
  }

  @Test
  @Timeout(10)
  void shouldProducePartsOverTime() {
    Thread factoryThread = new Thread(factory);
    factoryThread.start();

    await().atMost(5, TimeUnit.SECONDS)
        .until(() -> factory.getCurrentDay() > 3);

    assertThat(factory.getCurrentDay()).isGreaterThan(3);

    factory.stop();
    factoryThread.interrupt();
  }
}
