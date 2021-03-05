package de.hhu.propra.uav.domains.student;

import lombok.Data;
import org.springframework.data.annotation.Id;

@Data
public class Student {
  @Id
  private Long id = null;
  private final String github;

  @Override
  public String toString() {
    return github;
  }
}
