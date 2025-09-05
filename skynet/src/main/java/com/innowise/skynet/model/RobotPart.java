package com.innowise.skynet.model;

import lombok.*;
/**
 * Enum representing different types of robot parts.
 */
@ToString
@Getter
@AllArgsConstructor
public enum RobotPart {
  HEAD("Head"),
  TORSO("Torso"),
  HAND("Hand"),
  FEET("Feet");

  private final String name;

}
