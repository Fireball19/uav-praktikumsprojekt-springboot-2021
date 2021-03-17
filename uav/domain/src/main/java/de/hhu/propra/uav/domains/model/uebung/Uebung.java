package de.hhu.propra.uav.domains.model.uebung;

import de.hhu.propra.uav.domains.model.student.Student;
import lombok.Data;
import lombok.Getter;
import org.apache.tomcat.jni.Local;
import org.springframework.data.annotation.Id;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;
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
  @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm")
  private final LocalDateTime anmeldebeginn;
  @FutureOrPresent(message = "Das Datum muss in der Zukunft oder Gegenwart liegen")
  @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm")
  private final LocalDateTime anmeldeschluss;
  private boolean bearbeitet;
  private List<Termin> termine = new ArrayList<>();

  public void addTermin(final String tutor, final LocalDateTime zeitpunkt) {
    termine.add(new Termin(zeitpunkt, this.minGroesse, this.maxGroesse, tutor));
  }

  public void deleteTermin(final Long id) {
    termine.removeIf(termin -> termin.getId().equals(id));
  }

  public List<Gruppe> getGruppen() {
    List<Gruppe> gruppen= new ArrayList<>();
    for (Termin termin : termine) {
      gruppen.add(new Gruppe(termin.getGruppenname(), termin.getStudenten()));
    }
    return gruppen;
  }

  public List<Termin> getTermine() {
    return termine.stream()
        .sorted(Comparator.comparing(Termin::getZeitpunkt))
        .collect(Collectors.toList());
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

  public List<Termin> getRestplaetze() {
    return termine.stream()
        .filter(x -> x.getStudenten().size() < maxGroesse)
        .collect(Collectors.toList());
  }

  private void deleteStudent(final Student student, final Termin termin) {
    termin.deleteStudent(student);
  }

  @SuppressWarnings("PMD")
  public void moveStudent(final Student student, final Long terminAltId, final Long terminNeuId) {
    final Termin terminAlt = findTermin(terminAltId);
    final Termin terminNeu = findTermin(terminNeuId);
    if (terminAlt != null && terminNeu != null) {
      deleteStudent(student, terminAlt);
      addStudent(student, terminNeu);
    }
  }

  @SuppressWarnings("PMD")
  public void addGruppe(final String gruppenname, final Long terminId) {
    final Termin termin = findTermin(terminId);
    if (termin == null) {
      return;
    }
    termin.addGruppe(gruppenname);
  }

  @SuppressWarnings("PMD")
  public void deleteGruppe(final Long terminId) {
    final Termin termin = findTermin(terminId);
    if (termin == null) {
      return;
    }
    termin.deleteGruppe();
  }

  @SuppressWarnings("PMD")
  public boolean terminContainsStudent(final long terminId, final Student student) {
    Termin termin = findTermin(terminId);
    return termin.containsStudent(student);
  }

  public boolean containsStudent(final Student student) {
    for(Termin termin: termine) {
      if(termin.containsStudent(student)) {
        return true;
      }
    }
    return false;
  }

  @SuppressWarnings("PMD")
  public Termin findTermin(final Long terminId) {
    for (final Termin t : termine) {
      if (t.getId().equals(terminId)) {
        return t;
      }
    }
    return null;
  }

  public void abschliessen() {
      this.bearbeitet = true;
  }

  public Map<LocalDateTime,Integer> getKapazitaeten(){
    Map<LocalDateTime,Integer> localDateTimeMap = new HashMap<>();
    for(Termin termin : termine){
        localDateTimeMap.merge(termin.getZeitpunkt(),termin.getKapazitaet(),Integer::sum);
    }

    return localDateTimeMap;
  }

  public boolean hasTerminFreiePlaetze(final Long terminId) {
    return findTermin(terminId).getKapazitaet() > 0;
  }

  public List<Long> filterTerminIdsByZeitpunkt(final LocalDateTime zeitpunkt) {
    return termine.stream().filter(x -> x.getZeitpunkt()
        .isEqual(zeitpunkt))
        .mapToLong(Termin::getId)
        .boxed()
        .collect(Collectors.toList());
  }

  public List<Long> filterTerminIdsByStudent(final Student student) {
    return termine.stream().filter(x -> !x.containsStudent(student))
        .mapToLong(Termin::getId)
        .boxed()
        .collect(Collectors.toList());
  }

  public List<Daten> getTerminDatenIndividualmodus() {
    List<Daten> daten = new ArrayList<>();
    for (Termin termin : termine) {
      if (!termin.getStudenten().isEmpty()) {
        daten.add(new Daten(termin.getTutor(), termin.getZeitpunkt(), termin.getStudenten()));
      }
    }
    return daten;
  }
}