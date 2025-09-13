package com.innowise.skynet.model;

import lombok.ToString;
import lombok.Getter;
import lombok.AllArgsConstructor;

/**
 * Enumeration representing different types of robot parts in the SkyNet simulation.
 * <p>
 * Each robot must consist of four main parts: head, torso, hand, and feet. To create one robot, one
 * part of each type is required.
 * </p>
 */
@ToString
@Getter
@AllArgsConstructor
public enum RobotPart {

  /**
   * Robot head part
   */
  HEAD("Head"),

  /**
   * Robot torso part
   */
  TORSO("Torso"),

  /**
   * Robot hand part
   */
  HAND("Hand"),

  /**
   * Robot feet part
   */
  FEET("Feet");

  /**
   * Display name of the robot part.
   */
  private final String name;

}
