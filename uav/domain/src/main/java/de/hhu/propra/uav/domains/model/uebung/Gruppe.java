package de.hhu.propra.uav.domains.model.uebung;

import de.hhu.propra.uav.domains.model.student.StudentRef;
import lombok.Data;

import java.util.List;

@Data
public class Gruppe {
  private final String gruppenname;
  private final List<StudentRef> mitglieder;
}
