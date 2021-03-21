package de.hhu.propra.uav.domains.model.uebung;

import static org.assertj.core.api.Assertions.assertThat;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import org.junit.jupiter.api.Test;
import org.springframework.test.context.ActiveProfiles;

@ActiveProfiles("test")
public class UebungFactoryTests {

  private Uebung getUebung() {
    Uebung uebung = new Uebung("AlteTestUebung", Modus.GRUPPENANMELDUNG, 1, 5,
        LocalDateTime.of(2021, 1, 2, 12, 30),
        LocalDateTime.of(2021, 1, 5, 12, 30));

    LocalDateTime jetzt = LocalDateTime.now();
    uebung.addTermin("Alex",jetzt);
    uebung.addTermin("Bob",jetzt);
    Termin termin1 = uebung.getTermine().get(0);
    termin1.setId(1L);
    Termin termin2 = uebung.getTermine().get(1);
    termin2.setId(2L);

    return uebung;
  }

  @Test
  public void createDefaultTest(){
    Uebung uebung = UebungFactory.createDefault();

    assertThat(uebung.getName()).isEqualTo("DEFAULT");
    assertThat(uebung.getAnmeldeschluss().minus(1,ChronoUnit.WEEKS))
        .isEqualTo(uebung.getAnmeldebeginn());
  }

  @Test
  public void createWithAlteTermine(){
    Uebung alteUebung = getUebung();

    Uebung neueUebung = UebungFactory
        .createWithAlteTermine("NeueTestUebung", Modus.GRUPPENANMELDUNG, 2, 4,
            LocalDateTime.of(2021, 1, 2, 12, 30),
            LocalDateTime.of(2021, 1, 5, 12, 30),
            alteUebung);

    assertThat(neueUebung.getName()).isEqualTo("NeueTestUebung");
    assertThat(neueUebung.getMinGroesse()).isEqualTo(2);
    assertThat(neueUebung.getMaxGroesse()).isEqualTo(4);
    assertThat(neueUebung.getTermine().size()).isEqualTo(2);
    assertThat(neueUebung.getTermine().get(0).getZeitpunkt().minus(1, ChronoUnit.WEEKS))
        .isEqualTo(alteUebung.getTermine().get(0).getZeitpunkt());
  }


}
