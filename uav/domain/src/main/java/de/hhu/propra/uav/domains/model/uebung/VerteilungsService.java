package de.hhu.propra.uav.domains.model.uebung;

import de.hhu.propra.uav.domains.annotations.DomainService;
import de.hhu.propra.uav.domains.model.student.StudentRef;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@SuppressWarnings({"PMD.AtLeastOneConstructor", "PMD.AvoidDuplicateLiterals"})
@DomainService
public class VerteilungsService {

  @SuppressWarnings({"PMD.LawOfDemeter", "PMD.DataflowAnomalyAnalysis"})
  public void tutorenVerteilen(final Uebung uebung) {
    final List<Termin> termine = uebung.getTermine();
    final Map<LocalDateTime, List<Termin>> map = termine.stream()
        .collect(Collectors.groupingBy(Termin::getZeitpunkt));
    uebung.getTermine().clear();

    for (final LocalDateTime zeitpunkt : map.keySet()) {
      zufallsverteilung(map.get(zeitpunkt));
      termine.addAll(map.get(zeitpunkt));
    }
  }

  @SuppressWarnings({"PMD.LawOfDemeter", "PMD.DataflowAnomalyAnalysis"})
  private void zufallsverteilung(final List<Termin> termine) {
    final List<String> tutoren = new ArrayList<>();

    for (final Termin termin : termine) {
      tutoren.add(termin.getTutor());
    }

    Collections.shuffle(tutoren);
    int counter = 0;

    for (final Termin termin : termine) {
      termin.setTutor(tutoren.get(counter));
      counter++;
    }
  }

  @SuppressWarnings({"PMD.LawOfDemeter", "PMD.DataflowAnomalyAnalysis"})
  public void perfekteVerteilung(final Uebung uebung) {
    final List<Termin> termine = uebung.getTermine();
    final Map<LocalDateTime, List<Termin>> map = termine.stream()
        .collect(Collectors.groupingBy(Termin::getZeitpunkt));
    uebung.getTermine().clear();

    for (final LocalDateTime zeitpunkt : map.keySet()) {
      final List<Termin> terminList = map.get(zeitpunkt);
      int studentenAnzahl = 0;
      for (final Termin termin : terminList) {
        studentenAnzahl += termin.getStudenten().size();
      }

      int gruppenanzahl = studentenAnzahl / uebung.getMaxGroesse();
      if (studentenAnzahl % uebung.getMaxGroesse() != 0) {
        gruppenanzahl++;
      }

      final List<Termin> neueVerteilung = verteilung(terminList, gruppenanzahl, studentenAnzahl);
      uebung.getTermine().addAll(neueVerteilung);
    }

    uebung.abschliessen();
  }

  @SuppressWarnings({"PMD.LawOfDemeter", "PMD.DataflowAnomalyAnalysis", "PMD.OnlyOneReturn"})
  private List<Termin> verteilung(final List<Termin> terminList, final int gruppenanzahl, final int studentenanzahl) {
    if (studentenanzahl == 0) {
      return terminList;
    }
    final List<StudentRef> studenten = new ArrayList<>();
    final List<Termin> termineCopy = new ArrayList<>(terminList);
    for (final Termin termin : termineCopy) {
      studenten.addAll(termin.getStudenten());
      termin.getStudenten().clear();
    }
    Collections.shuffle(studenten);

    int index = 0;
    int offset = 0;

    for (int i = 0; i < studentenanzahl % gruppenanzahl; i++) {
      for (int j = 0; j < (studentenanzahl / gruppenanzahl) + 1; j++) {
        termineCopy.get(i).getStudenten().add(studenten.get(j));
        offset++;
      }
      index++;
    }


    for (int i = 0; i < gruppenanzahl - (studentenanzahl % gruppenanzahl); i++) {
      for (int j = 0; j < (studentenanzahl / gruppenanzahl); j++) {
        termineCopy.get(index + i).getStudenten().add(studenten.get(offset));
        offset++;
      }
    }

    return termineCopy;
  }
}
