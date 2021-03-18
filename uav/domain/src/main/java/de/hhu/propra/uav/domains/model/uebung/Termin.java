package de.hhu.propra.uav.domains.model.uebung;

import de.hhu.propra.uav.domains.model.student.Student;
import de.hhu.propra.uav.domains.model.student.StudentRef;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@SuppressWarnings({"PMD.CommentDefaultAccessModifier", "PMD.DefaultPackage"})
@Data
class Termin {
  @SuppressWarnings("PMD")
  @Id
  private Long id = null;
  @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm")
  private final LocalDateTime zeitpunkt;
  private String gruppenname = "";
  private boolean reserviert;
  private final int minGroesse;
  private final int maxGroesse;
  private String tutor;
  private List<StudentRef> studenten = new ArrayList<>();

  Termin(final LocalDateTime zeitpunkt, final int minGroesse, final int maxGroesse, final String tutor) {
    this.zeitpunkt = zeitpunkt;
    this.minGroesse = minGroesse;
    this.maxGroesse = maxGroesse;
    this.tutor = tutor;
  }

  void addGruppe(final String gruppenname) {
    this.reserviert = true;
    this.gruppenname = gruppenname;
  }

  void deleteGruppe() {
    this.gruppenname = "";
    this.reserviert = false;
    studenten.clear();
  }

  void addStudent(final Student student) {
    studenten.add(new StudentRef(student.getId()));
  }

  void deleteStudent(final Student student) {
    studenten.remove(new StudentRef(student.getId()));
  }

  boolean containsStudent(final Student student) {
    return studenten.contains(new StudentRef(student.getId()));
  }

  int getKapazitaet() {
    return maxGroesse - studenten.size();
  }
}
