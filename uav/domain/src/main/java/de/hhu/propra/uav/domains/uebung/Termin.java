package de.hhu.propra.uav.domains.uebung;

import de.hhu.propra.uav.domains.student.Student;
import de.hhu.propra.uav.domains.student.StudentRef;
import lombok.Data;
import org.springframework.data.annotation.Id;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Data
public class Termin {
  @Id
  private Long id = null;
  private final LocalDateTime zeitpunkt;
  private boolean reserviert;
  private int minGroesse;
  private int maxGroesse;
  private final String tutor;
  private List<StudentRef> studenten = new ArrayList<>();

  public Termin(LocalDateTime zeitpunkt, int minGroesse, int maxGroesse, String tutor){
    this.zeitpunkt = zeitpunkt;
    this.minGroesse = minGroesse;
    this.maxGroesse = maxGroesse;
    this.tutor = tutor;
  }

  public void addStudent(final Student student) {
    studenten.add(new StudentRef(student.getId()));
  }

  public void deleteStudent(final Student student) {
    studenten.remove(new StudentRef(student.getId()));
  }

  @Override
  public String toString() {
    return tutor + " | " + zeitpunkt + " | " + reserviert + " | " + studenten;
  }
}
