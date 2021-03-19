package de.hhu.propra.uav.domains.model.student;

import lombok.Data;
import org.springframework.data.annotation.Id;

@Data
@SuppressWarnings("PMD")
public class Student {

  @Id
  private Long id = null;
  private final String github;
}
