package de.hhu.propra.uav.domains.model.uebung;

import de.hhu.propra.uav.domains.model.student.StudentRef;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
public class TerminInfoDTO {
  private final String tutor;
  private final LocalDateTime zeitpunkt;
  private final List<StudentRef> studenten;
}
