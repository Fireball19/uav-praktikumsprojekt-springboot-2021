package de.hhu.propra.uav.domains.model.uebung;

import de.hhu.propra.uav.domains.model.student.StudentRef;
import java.util.List;
import lombok.Data;


@Data
public class GruppeDto {

  private final String gruppenname;
  private final List<StudentRef> mitglieder;
}
