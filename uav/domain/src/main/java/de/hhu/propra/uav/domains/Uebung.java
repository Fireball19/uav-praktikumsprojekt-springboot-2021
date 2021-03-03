package de.hhu.propra.uav.domains;

import lombok.Data;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Data
public class Uebung {
  private final String name;
  private final Modus modus;
  private final Integer minGroesse;
  private final Integer maxGroesse;
  private final LocalDateTime anmeldebeginn;
  private final LocalDateTime anmeldeschluss;
  private List<Termin> termine = new ArrayList<>();

  public void addTermin(String tutor, LocalDateTime zeitpunkt) {
    termine.add(new Termin(tutor, zeitpunkt, this.minGroesse, this.maxGroesse));
  }

  public void addStudent(Student student, LocalDateTime zeitpunkt, String tutor) {
    Termin termin = findTermin(zeitpunkt, tutor);
    if (termin == null) {
      return;
    }
    termin.addStudent(student);
  }

  private void addStudent(Student student, Termin termin) {
    termin.addStudent(student);
  }

  public void deleteStudent(Student student, LocalDateTime zeitpunkt, String tutor) {
    Termin termin = findTermin(zeitpunkt, tutor);
    if (termin == null) {
      return;
    }
    termin.deleteStudent(student);
  }

  private void deleteStudent(Student student, Termin termin) {
    termin.deleteStudent(student);
  }

  public void moveStudent(Student student, LocalDateTime zeitpunkt, String tutor, LocalDateTime zeitpunkt2, String tutor2) {
    Termin termin1 = findTermin(zeitpunkt,tutor);
    Termin termin2 = findTermin(zeitpunkt2,tutor2);
    if(termin1 != null && termin2 != null) {
      addStudent(student,termin2);
      deleteStudent(student,termin1);
    }
  }

  private Termin findTermin(LocalDateTime zeitpunkt, String tutor) {
    for (Termin t : termine) {
      if (t.getZeitpunkt().equals(zeitpunkt) && t.getTutor().equals(tutor)) {
        return t;
      }
    }
    return null;
  }
}