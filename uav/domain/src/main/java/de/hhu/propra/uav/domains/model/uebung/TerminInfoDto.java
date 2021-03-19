package de.hhu.propra.uav.domains.model.uebung;

import de.hhu.propra.uav.domains.model.student.StudentRef;
import java.time.LocalDateTime;
import java.util.List;
import lombok.Data;

@Data
public class TerminInfoDto {

  private final String tutor;
  private final LocalDateTime zeitpunkt;
  private final List<StudentRef> studenten;
}
