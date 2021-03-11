package de.hhu.propra.uav.domains.services;

import de.hhu.propra.uav.domains.annotations.ApplicationService;
import de.hhu.propra.uav.domains.model.uebung.Uebung;
import org.springframework.http.HttpStatus;
import org.springframework.web.client.HttpClientErrorException;

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



  public void gruppenAnmeldung(final Long uebungId, final Long terminId,
                               final String gruppenname, final String mitglieder) {
    final String[] split = mitglieder.split(",");
    if(split.length > uebungService.ueberpruefeMaxGroesse(uebungId)) {
      throw new HttpClientErrorException(HttpStatus.CONFLICT, "Die Anzahl der eingetragenen Mitglieder " +
          "übersteigt die maximale Gruppengröße " + uebungService.ueberpruefeMaxGroesse(uebungId) + "!");
    }
    uebungService.addGruppe(uebungId, terminId, gruppenname);
    for (String s : split) {
      s = s.trim();
      verwaltungService.addStudent(s, uebungId, terminId);
    }
  }
}
