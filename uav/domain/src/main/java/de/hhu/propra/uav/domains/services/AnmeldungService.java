package de.hhu.propra.uav.domains.services;

import de.hhu.propra.uav.domains.annotations.ApplicationService;
import de.hhu.propra.uav.domains.model.uebung.Uebung;

import java.util.Arrays;

@ApplicationService
public class AnmeldungService {


  private final UebungService uebungService;
  private final VerwaltungService verwaltungService;
  private final GithubAPIService githubAPIService;

  public AnmeldungService(UebungService uebungService, VerwaltungService verwaltungService) {
    this.uebungService = uebungService;
    this.verwaltungService = verwaltungService;
    this.githubAPIService = githubAPIService;
  }

  public void gruppenAnmeldung(final Long uebungId, final Long terminId,
                               final String gruppenname, final String mitglieder) {
    uebungService.addGruppe(uebungId, terminId, gruppenname);
    final String[] split = mitglieder.split(",");
    for (String s : split) {
      s = s.trim();
      verwaltungService.addStudent(s, uebungId, terminId);
    }
  }
}
