package de.hhu.propra.uav.domains;

import lombok.Data;

@Data
public class Student {
  private final String github;

  @Override
  public String toString() {
    return github;
  }
}
