package de.hhu.propra.uav.domains.model.uebung;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

public class UebungFactory {

  public static Uebung createDefault() {
    return new Uebung("DEFAULT", Modus.DEFAULT, 0, 0,
        LocalDateTime.now(),
        LocalDateTime.now().plus(1, ChronoUnit.WEEKS));
  }

  public static Uebung createWithAlteTermine(final String name, final Modus modus, final int minGroesse,
                                             final int maxGroesse,
                                             final LocalDateTime anmeldebeginn, final LocalDateTime anmeldeschluss,
                                             final Uebung alteUebung) {
    Uebung uebung = new Uebung(name, modus, minGroesse, maxGroesse, anmeldebeginn, anmeldeschluss);
    for (Termin termin: alteUebung.getTermine()) {
      uebung.addTermin(termin.getTutor(), termin.getZeitpunkt().plus(1, ChronoUnit.WEEKS));
    }
    return uebung;
  }
}
