package de.hhu.propra.uav.domains;

import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import javax.validation.constraints.Positive;


@SuppressWarnings("LossyEncoding")
@Data
public class Uebung {
  private final String name;
  private final Modus modus;
  @Positive(message = "Die Gruppengr��e muss positiv sein!")
  private final Integer minGroesse;
  @Positive(message = "Die Gruppengr��e muss positiv sein!")
  private final Integer maxGroesse;
  @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm")
  private final LocalDateTime anmeldebeginn;
  @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm")
  private final LocalDateTime anmeldeschluss;
  private final List<Termin> termine = new ArrayList<>();

  public void addTermin(final String tutor, final LocalDateTime zeitpunkt) {
    termine.add(new Termin(tutor, zeitpunkt, this.minGroesse, this.maxGroesse));
  }

  public void addStudent(final Student student, final LocalDateTime zeitpunkt, final String tutor) {
    final Termin termin = findTermin(zeitpunkt, tutor);
    if (termin == null) {
      return;
    }
    termin.addStudent(student);
  }

  private void addStudent(final Student student, final Termin termin) {
    termin.addStudent(student);
  }

  public void deleteStudent(final Student student, final LocalDateTime zeitpunkt, final String tutor) {
    final Termin termin = findTermin(zeitpunkt, tutor);
    if (termin == null) {
      return;
    }
    termin.deleteStudent(student);
  }

  private void deleteStudent(final Student student, final Termin termin) {
    termin.deleteStudent(student);
  }

  public void moveStudent(final Student student, final LocalDateTime zeitpunkt, final String tutor,
                          final LocalDateTime zeitpunkt2, final String tutor2) {
    final Termin termin1 = findTermin(zeitpunkt, tutor);
    final Termin termin2 = findTermin(zeitpunkt2, tutor2);
    if (termin1 != null && termin2 != null) {
      addStudent(student, termin2);
      deleteStudent(student, termin1);
    }
  }

  private Termin findTermin(final LocalDateTime zeitpunkt, final String tutor) {
    for (final Termin t : termine) {
      if (t.getZeitpunkt().equals(zeitpunkt) && t.getTutor().equals(tutor)) {
        return t;
      }
    }
    return null;
  }
}