package de.hhu.propra.uav.domains.services;

import de.hhu.propra.uav.domains.annotations.ApplicationService;
import de.hhu.propra.uav.domains.model.uebung.Uebung;
import org.springframework.http.HttpStatus;
import org.springframework.web.client.HttpClientErrorException;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

@ApplicationService
public class AnmeldungService {


  private final UebungService uebungService;
  private final VerwaltungService verwaltungService;

  public AnmeldungService(UebungService uebungService, VerwaltungService verwaltungService) {
    this.uebungService = uebungService;
    this.verwaltungService = verwaltungService;
  }

  public void restAnmeldung(final Long uebungId, final Long terminId, final String mitglied) {
    verwaltungService.addStudent(mitglied,uebungId,terminId);
  }

  public void individualAnmeldung(final Long uebungId, final LocalDateTime zeitpunkt, final String student) {
    Uebung uebung = uebungService.findById(uebungId);
    List<Long> terminIds = uebung.filterTerminIdsByZeitpunkt(zeitpunkt);

    for (Long terminId : terminIds) {
      if (uebung.hasTerminFreiePlaetze(terminId)) {
        verwaltungService.addStudent(student, uebungId, terminId);
        break;
      }
    }
  }

  public void gruppenAnmeldung(final Long uebungId, final Long terminId,
                               final String gruppenname, final String mitglieder) {
    final String[] split = mitglieder.split(",");
    if(split.length < uebungService.ueberpruefeMinGroesse(uebungId)) {
      throw new HttpClientErrorException(HttpStatus.CONFLICT, "Die Anzahl der eingetragenen Mitglieder " +
          "liegt unter der minimalen Gruppengröße " + uebungService.ueberpruefeMinGroesse(uebungId) + "!");
    }
    if(split.length > uebungService.ueberpruefeMaxGroesse(uebungId)) {
      throw new HttpClientErrorException(HttpStatus.CONFLICT, "Die Anzahl der eingetragenen Mitglieder " +
          "übersteigt die maximale Gruppengröße " + uebungService.ueberpruefeMaxGroesse(uebungId) + "!");
    }
    for (String s : split) {
      s = s.trim();
      verwaltungService.addStudent(s, uebungId, terminId);
    }

    uebungService.addGruppe(uebungId, terminId, gruppenname);
  }
}
