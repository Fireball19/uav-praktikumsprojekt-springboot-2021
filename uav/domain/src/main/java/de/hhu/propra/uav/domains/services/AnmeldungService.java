package de.hhu.propra.uav.domains.services;

import de.hhu.propra.uav.domains.annotations.ApplicationService;

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
    uebungService.addGruppe(uebungId, terminId, gruppenname);
    final String[] split = mitglieder.split(",");
    for (String s : split) {
      verwaltungService.addStudent(s, uebungId, terminId);
    }
  }
}
