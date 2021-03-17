package de.hhu.propra.uav.domains.model.uebung;

import de.hhu.propra.uav.domains.annotations.DomainService;
import de.hhu.propra.uav.domains.model.student.StudentRef;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@DomainService
public class VerteilungsService {

  public void tutorenVerteilen(Uebung uebung) {
    List<Termin> termine = uebung.getTermine();
    Map<LocalDateTime, List<Termin>> map = termine.stream()
        .collect(Collectors.groupingBy(Termin::getZeitpunkt));
    uebung.getTermine().clear();
    for (LocalDateTime zeitpunkt : map.keySet()) {
      zufallsverteilung(map.get(zeitpunkt));
      termine.addAll(map.get(zeitpunkt));
    }
   }

  private void zufallsverteilung(List<Termin> termine) {
    List<String> tutoren = new ArrayList<>();
    for (Termin termin : termine) {
      tutoren.add(termin.getTutor());
    }
    Collections.shuffle(tutoren);
    int i = 0;
    for (Termin termin : termine) {
      termin.setTutor(tutoren.get(i));
      i++;
    }
  }

  public void perfekteVerteilung(Uebung uebung) {
    List<Termin> termine = uebung.getTermine();
    Map<LocalDateTime, List<Termin>> map = termine.stream()
        .collect(Collectors.groupingBy(Termin::getZeitpunkt));
    uebung.getTermine().clear();

    for (LocalDateTime zeitpunkt : map.keySet()) {
      List<Termin> terminList = map.get(zeitpunkt);
      int studentenAnzahl = 0;
      for (Termin termin : terminList) {
        studentenAnzahl += termin.getStudenten().size();
      }

      int gruppenanzahl = studentenAnzahl / uebung.getMaxGroesse();
      if (studentenAnzahl % uebung.getMaxGroesse() != 0) {
        gruppenanzahl++;
      }

      List<Termin> neueVerteilung = verteilung(terminList, gruppenanzahl, studentenAnzahl);
      uebung.getTermine().addAll(neueVerteilung);
    }

    uebung.abschliessen();
  }

  private List<Termin> verteilung(List<Termin> terminList, int gruppenanzahl, int studentenanzahl) {
    if(studentenanzahl == 0) {
      return terminList;
    }
    List<StudentRef> studenten = new ArrayList<>();
    List<Termin> termineCopy = new ArrayList<>(terminList);
    for (Termin termin : termineCopy) {
      studenten.addAll(termin.getStudenten());
      termin.getStudenten().clear();
    }
    Collections.shuffle(studenten);

    int index = 0;
    int offset = 0;

    for(int i = 0; i < studentenanzahl % gruppenanzahl; i++) {
      for(int j = 0; j < (studentenanzahl / gruppenanzahl) + 1; j++) {
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
