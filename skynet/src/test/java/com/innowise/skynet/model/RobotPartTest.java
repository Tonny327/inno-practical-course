package com.innowise.skynet.model;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Unit tests for RobotPart enum.
 */
class RobotPartTest {

  @Test
  void shouldHaveCorrectNumberOfParts() {
    assertThat(RobotPart.values()).hasSize(4);
  }

  @Test
  void shouldHaveCorrectPartNames() {
    assertThat(RobotPart.HEAD.getName()).isEqualTo("Head");
    assertThat(RobotPart.TORSO.getName()).isEqualTo("Torso");
    assertThat(RobotPart.HAND.getName()).isEqualTo("Hand");
    assertThat(RobotPart.FEET.getName()).isEqualTo("Feet");
  }

  @Test
  void shouldReturnCorrectToString() {
    assertThat(RobotPart.HEAD.toString()).isEqualTo("RobotPart.HEAD(name=Head)");
    assertThat(RobotPart.TORSO.toString()).isEqualTo("RobotPart.TORSO(name=Torso)");
    assertThat(RobotPart.HAND.toString()).isEqualTo("RobotPart.HAND(name=Hand)");
    assertThat(RobotPart.FEET.toString()).isEqualTo("RobotPart.FEET(name=Feet)");
  }

  @Test
  void shouldGetCorrectDisplayName() {
    assertThat(RobotPart.HEAD.getName()).isEqualTo("Head");
    assertThat(RobotPart.TORSO.getName()).isEqualTo("Torso");
    assertThat(RobotPart.HAND.getName()).isEqualTo("Hand");
    assertThat(RobotPart.FEET.getName()).isEqualTo("Feet");
  }
}