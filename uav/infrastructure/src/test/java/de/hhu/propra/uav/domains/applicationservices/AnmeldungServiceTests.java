package de.hhu.propra.uav.domains.applicationservices;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import de.hhu.propra.uav.domains.model.uebung.Modus;
import de.hhu.propra.uav.domains.model.uebung.Uebung;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.web.client.HttpClientErrorException;

@ExtendWith(MockitoExtension.class)
@ActiveProfiles("test")
public class AnmeldungServiceTests {

  @Mock
  UebungService uebungService;
  @Mock
  VerwaltungService verwaltungService;

  @Test
  public void restAnmeldungTest() {
    AnmeldungService anmeldungService = new AnmeldungService(uebungService, verwaltungService);
    anmeldungService.restAnmeldung(1L, 2L, "Alex");

    verify(verwaltungService, times(1)).addStudent("Alex", 1L, 2L);
  }

  @Test
  public void gruppenAnmeldungThrowsMinException() {
    AnmeldungService anmeldungService = new AnmeldungService(uebungService, verwaltungService);
    Uebung testUebung = new Uebung("TestUebung", Modus.GRUPPENANMELDUNG, 2, 3,
        LocalDateTime.now().minus(10, ChronoUnit.MINUTES),
        LocalDateTime.now().plus(10, ChronoUnit.MINUTES));
    when(uebungService.ueberpruefeMinGroesse(any())).thenReturn(testUebung.getMinGroesse());
    assertThrows(HttpClientErrorException.class,
        () -> {
          anmeldungService.gruppenAnmeldung(1L, 1L,
              "TeamTest", "Alex");
        });
  }

  @Test
  public void gruppenAnmeldungThrowsMaxException() {
    AnmeldungService anmeldungService = new AnmeldungService(uebungService, verwaltungService);
    Uebung testUebung = new Uebung("TestUebung", Modus.GRUPPENANMELDUNG, 2, 3,
        LocalDateTime.now().minus(10, ChronoUnit.MINUTES),
        LocalDateTime.now().plus(10, ChronoUnit.MINUTES));
    when(uebungService.ueberpruefeMinGroesse(any())).thenReturn(testUebung.getMinGroesse());
    when(uebungService.ueberpruefeMaxGroesse(any())).thenReturn(testUebung.getMaxGroesse());

    assertThrows(HttpClientErrorException.class,
        () -> {
          anmeldungService.gruppenAnmeldung(1L, 1L,
              "TeamTest", "Alex,Bob,Charlie,David");
        });
  }

  @Test
  public void gruppenAnmeldungThrowsNoException() {
    AnmeldungService anmeldungService = new AnmeldungService(uebungService, verwaltungService);
    Uebung testUebung = new Uebung("TestUebung", Modus.GRUPPENANMELDUNG, 2, 3,
        LocalDateTime.now().minus(10, ChronoUnit.MINUTES),
        LocalDateTime.now().plus(10, ChronoUnit.MINUTES));
    when(uebungService.ueberpruefeMinGroesse(any())).thenReturn(testUebung.getMinGroesse());
    when(uebungService.ueberpruefeMaxGroesse(any())).thenReturn(testUebung.getMaxGroesse());

    anmeldungService.gruppenAnmeldung(1L, 1L, "TeamTest", "Alex,Bob,Charlie");

    verify(uebungService, times(1)).addGruppe(any(), any(), any());
  }

  @Test
  public void individualAnmeldungTest()
      throws ClassNotFoundException, NoSuchMethodException, InvocationTargetException, IllegalAccessException {
    Uebung testUebung = new Uebung("TestUebung", Modus.GRUPPENANMELDUNG, 2, 3,
        LocalDateTime.now().minus(10, ChronoUnit.MINUTES),
        LocalDateTime.now().plus(10, ChronoUnit.MINUTES));
    LocalDateTime jetzt = LocalDateTime.now();
    testUebung.addTermin("Alex",jetzt);
    testUebung.addTermin("Bob",jetzt);
    testUebung.addTermin("Charlie",jetzt);

    Class clazz = Class.forName("de.hhu.propra.uav.domains.model.uebung.Termin");
    Method method = clazz.getDeclaredMethod("setId", Long.class);
    method.setAccessible(true);

    method.invoke(testUebung.getTermine().get(0),1L);
    method.invoke(testUebung.getTermine().get(1),2L);
    method.invoke(testUebung.getTermine().get(2),3L);

    AnmeldungService anmeldungService = new AnmeldungService(uebungService,verwaltungService);

    when(uebungService.findById(any())).thenReturn(testUebung);

    anmeldungService.individualAnmeldung(1L,jetzt,"student");

    verify(verwaltungService,times(1)).addStudent("student",1L,1L);

  }
}
