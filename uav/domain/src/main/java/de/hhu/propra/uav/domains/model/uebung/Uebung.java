package de.hhu.propra.uav.domains.model.uebung;

import de.hhu.propra.uav.domains.model.student.Student;
import lombok.Data;
import lombok.Getter;
import org.springframework.data.annotation.Id;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import javax.validation.constraints.FutureOrPresent;
import javax.validation.constraints.Positive;


@SuppressWarnings("LossyEncoding")
@Data
public class Uebung {

  @Id
  @Getter
  @SuppressWarnings("PMD")
  private Long id = null;
  private final String name;
  private final Modus modus;
  @Positive(message = "Die Anzahl muss mindestens 1 sein")
  private final Integer minGroesse;
  @Positive(message = "Die Anzahl muss mindestens 1 sein")
  private final Integer maxGroesse;
  @FutureOrPresent(message = "Das Datum muss in der Zukunft oder Gegenwart liegen")
  @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm")
  private final LocalDateTime anmeldebeginn;
  @FutureOrPresent(message = "Das Datum muss in der Zukunft oder Gegenwart liegen")
  @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm")
  private final LocalDateTime anmeldeschluss;
  private List<Termin> termine = new ArrayList<>();

  public void addTermin(final String tutor, final LocalDateTime zeitpunkt) {
    termine.add(new Termin(zeitpunkt, this.minGroesse, this.maxGroesse, tutor));
  }

  @SuppressWarnings("PMD")
  public void addStudent(final Student student, final Long terminId) {
    final Termin termin = findTermin(terminId);
    if (termin == null) {
      return;
    }
    termin.addStudent(student);
  }

  private void addStudent(final Student student, final Termin termin) {
    termin.addStudent(student);
  }

  @SuppressWarnings("PMD")
  public void deleteStudent(final Student student, final Long terminId) {
    final Termin termin = findTermin(terminId);
    if (termin == null) {
      return;
    }
    termin.deleteStudent(student);
  }

  private void deleteStudent(final Student student, final Termin termin) {
    termin.deleteStudent(student);
  }

  @SuppressWarnings("PMD")
  public void moveStudent(final Student student, final Long teminAltId, final Long terminNeuId) {
    final Termin terminAlt = findTermin(teminAltId);
    final Termin terminNeu = findTermin(terminNeuId);
    if (terminAlt != null && terminNeu != null) {
      addStudent(student, terminNeu);
      deleteStudent(student, terminAlt);
    }
  }

  @SuppressWarnings("PMD")
  private Termin findTermin(final Long terminId) {
    for (final Termin t : termine) {
      if (t.getId().equals(terminId)) {
        return t;
      }
    }
    return null;
  }
}