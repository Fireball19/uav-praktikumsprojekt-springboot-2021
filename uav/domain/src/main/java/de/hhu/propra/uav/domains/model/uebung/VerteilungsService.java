package de.hhu.propra.uav.domains.model.uebung;

import de.hhu.propra.uav.domains.annotations.DomainService;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
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
}
