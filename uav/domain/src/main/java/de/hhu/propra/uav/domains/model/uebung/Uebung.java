package de.hhu.propra.uav.domains.model.uebung;

import de.hhu.propra.uav.domains.model.student.Student;
import lombok.Data;
import lombok.Getter;
import org.springframework.data.annotation.Id;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;
import javax.validation.constraints.FutureOrPresent;
import javax.validation.constraints.Positive;

@SuppressWarnings({"PMD.TooManyMethods","PMD.AvoidDuplicateLiterals"})
@Data
public class Uebung {

  @SuppressWarnings({"PMD.RedundantFieldInitializer","PMD.ShortVariable"})
  @Id
  @Getter
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

  public void deleteTermin(final Long uebungId) {
    termine.removeIf(termin -> termin.getId().equals(uebungId));
  }

  public List<GruppeDTO> getGruppen() {
    final List<GruppeDTO> gruppen = new ArrayList<>();
    for (final Termin termin : termine) {
      gruppen.add(new GruppeDTO(termin.getGruppenname(), termin.getStudenten()));
    }
    return gruppen;
  }

  @SuppressWarnings("PMD.LawOfDemeter")
  public List<Termin> getTermine() {
    return termine.stream()
        .sorted(Comparator.comparing(Termin::getZeitpunkt))
        .collect(Collectors.toList());
  }

  @SuppressWarnings("PMD.LawOfDemeter")
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

  @SuppressWarnings("PMD.LawOfDemeter")
  public void deleteStudent(final Student student, final Long terminId) {
    final Termin termin = findTermin(terminId);
    if (termin == null) {
      return;
    }
    termin.deleteStudent(student);
  }

  @SuppressWarnings("PMD.LawOfDemeter")
  public List<Termin> getRestplaetze() {
    return termine.stream()
        .filter(x -> x.getStudenten().size() < maxGroesse)
        .collect(Collectors.toList());
  }

  private void deleteStudent(final Student student, final Termin termin) {
    termin.deleteStudent(student);
  }

  public void moveStudent(final Student student, final Long terminAltId, final Long terminNeuId) {
    final Termin terminAlt = findTermin(terminAltId);
    final Termin terminNeu = findTermin(terminNeuId);
    if (terminAlt != null && terminNeu != null) {
      deleteStudent(student, terminAlt);
      addStudent(student, terminNeu);
    }
  }

  @SuppressWarnings("PMD.LawOfDemeter")
  public void addGruppe(final String gruppenname, final Long terminId) {
    final Termin termin = findTermin(terminId);
    if (termin == null) {
      return;
    }
    termin.addGruppe(gruppenname);
  }

  @SuppressWarnings("PMD.LawOfDemeter")
  public void deleteGruppe(final Long terminId) {
    final Termin termin = findTermin(terminId);
    if (termin == null) {
      return;
    }
    termin.deleteGruppe();
  }

  @SuppressWarnings("PMD.LawOfDemeter")
  public boolean terminContainsStudent(final long terminId, final Student student) {
    final Termin termin = findTermin(terminId);
    return termin.containsStudent(student);
  }

  @SuppressWarnings("PMD.OnlyOneReturn")
  public boolean containsStudent(final Student student) {
    for(final Termin termin: termine) {
      if(termin.containsStudent(student)) {
        return true;
      }
    }
    return false;
  }

  @SuppressWarnings({"PMD.LawOfDemeter","PMD.OnlyOneReturn"})
  public Termin findTermin(final Long terminId) {
    for (final Termin termin : termine) {
      if (termin.getId().equals(terminId)) {
        return termin;
      }
    }
    return null;
  }

  public void abschliessen() {
      this.bearbeitet = true;
  }

  public Map<LocalDateTime,Integer> getKapazitaeten(){
    final Map<LocalDateTime,Integer> localDateTimeMap = new ConcurrentHashMap<>();
    for(final Termin termin : termine){
        localDateTimeMap.merge(termin.getZeitpunkt(),termin.getKapazitaet(),Integer::sum);
    }

    return localDateTimeMap;
  }

  @SuppressWarnings("PMD.LawOfDemeter")
  public boolean hasTerminFreiePlaetze(final Long terminId) {
    final Termin termin = findTermin(terminId);
    return termin.getKapazitaet() > 0;
  }

  @SuppressWarnings("PMD.LawOfDemeter")
  public List<Long> filterTerminIdsByZeitpunkt(final LocalDateTime zeitpunkt) {
    return termine.stream().filter(x -> x.getZeitpunkt()
        .isEqual(zeitpunkt))
        .mapToLong(Termin::getId)
        .boxed()
        .collect(Collectors.toList());
  }

  @SuppressWarnings("PMD.LawOfDemeter")
  public List<Long> filterTerminIdsByStudent(final Student student) {
    return termine.stream().filter(x -> !x.containsStudent(student))
        .mapToLong(Termin::getId)
        .boxed()
        .collect(Collectors.toList());
  }

  @SuppressWarnings("PMD.LawOfDemeter")
  public List<TerminInfoDTO> getTerminDatenIndividualmodus() {
    final List<TerminInfoDTO> terminInfoDTO = new ArrayList<>();
    for (final Termin termin : termine) {
      if (!termin.getStudenten().isEmpty()) {
        terminInfoDTO.add(new TerminInfoDTO(termin.getTutor(), termin.getZeitpunkt(), termin.getStudenten()));
      }
    }
    return terminInfoDTO;
  }
}