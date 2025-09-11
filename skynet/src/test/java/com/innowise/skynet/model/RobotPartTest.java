package com.innowise.skynet.model;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Unit tests for RobotPart enum.
 * <p>
 * Tests verify the correctness of the RobotPart enumeration including:
 * <ul>
 * <li>Correct number of enum values</li>
 * <li>Proper naming and display names</li>
 * <li>ToString method functionality</li>
 * </ul>
 * </p>
 */
class RobotPartTest {

  /**
   * Tests that RobotPart enum has exactly 4 values.
   */
  @Test
  void shouldHaveCorrectNumberOfParts() {
    assertThat(RobotPart.values()).hasSize(4);
  }

  /**
   * Tests that each RobotPart has the correct display name.
   */
  @Test
  void shouldHaveCorrectPartNames() {
    assertThat(RobotPart.HEAD.getName()).isEqualTo("Head");
    assertThat(RobotPart.TORSO.getName()).isEqualTo("Torso");
    assertThat(RobotPart.HAND.getName()).isEqualTo("Hand");
    assertThat(RobotPart.FEET.getName()).isEqualTo("Feet");
  }

  /**
   * Tests that the toString method returns the expected format.
   */
  @Test
  void shouldReturnCorrectToString() {
    assertThat(RobotPart.HEAD.toString()).isEqualTo("RobotPart.HEAD(name=Head)");
    assertThat(RobotPart.TORSO.toString()).isEqualTo("RobotPart.TORSO(name=Torso)");
    assertThat(RobotPart.HAND.toString()).isEqualTo("RobotPart.HAND(name=Hand)");
    assertThat(RobotPart.FEET.toString()).isEqualTo("RobotPart.FEET(name=Feet)");
  }

  /**
   * Tests that getName method returns the correct display name for each part.
   */
  @Test
  void shouldGetCorrectDisplayName() {
    assertThat(RobotPart.HEAD.getName()).isEqualTo("Head");
    assertThat(RobotPart.TORSO.getName()).isEqualTo("Torso");
    assertThat(RobotPart.HAND.getName()).isEqualTo("Hand");
    assertThat(RobotPart.FEET.getName()).isEqualTo("Feet");
  }
}