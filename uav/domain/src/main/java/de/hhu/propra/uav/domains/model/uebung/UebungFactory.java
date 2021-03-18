package de.hhu.propra.uav.domains.model.uebung;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

@SuppressWarnings({"PMD.UseUtilityClass","PMD.ClassNamingConventions"})
public class UebungFactory {

  @SuppressWarnings("PMD.LawOfDemeter")
  public static Uebung createDefault() {
    final LocalDateTime jetzt = LocalDateTime.now();
    return new Uebung("DEFAULT", Modus.DEFAULT, 0, 0,
        jetzt,
        jetzt.plus(1, ChronoUnit.WEEKS));
  }

  @SuppressWarnings("PMD.LawOfDemeter")
  public static Uebung createWithAlteTermine(final String name, final Modus modus, final int minGroesse,
                                             final int maxGroesse,
                                             final LocalDateTime anmeldebeginn, final LocalDateTime anmeldeschluss,
                                             final Uebung alteUebung) {
    final Uebung uebung = new Uebung(name, modus, minGroesse, maxGroesse, anmeldebeginn, anmeldeschluss);
    for (final Termin termin: alteUebung.getTermine()) {
      uebung.addTermin(termin.getTutor(), termin.getZeitpunkt().plus(1, ChronoUnit.WEEKS));
    }
    return uebung;
  }
}
