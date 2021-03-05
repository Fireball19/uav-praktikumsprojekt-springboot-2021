package de.hhu.propra.uav.domains;

import lombok.Data;
import lombok.Getter;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.data.annotation.Id;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Data
public class Termin {
  @Id
  private Long id = null;
  private Long uebung_key;
  private final LocalDateTime zeitpunkt;
  private boolean reserviert;
  @NonNull
  private int minGroesse;
  @NonNull
  private int maxGroesse;
  private final String tutor;
  private List<StudentRef> studenten = new ArrayList<>();


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
