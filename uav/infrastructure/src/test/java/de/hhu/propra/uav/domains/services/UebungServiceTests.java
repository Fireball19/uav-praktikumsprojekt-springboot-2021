package de.hhu.propra.uav.domains.services;

import de.hhu.propra.uav.domains.model.uebung.Modus;
import de.hhu.propra.uav.domains.model.uebung.Uebung;
import de.hhu.propra.uav.domains.model.uebung.UebungRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.client.HttpClientErrorException;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class UebungServiceTests {

  @Mock
  UebungRepository uebungRepository;

  @Test
  public void findByGithubThrowsException() {
    when(uebungRepository.findByName(anyString())).thenReturn(Optional.empty());

    UebungService uebungService = new UebungService(uebungRepository);
    assertThrows(HttpClientErrorException.class,
        () -> {
          uebungService.findByName("PU1");
        });
  }

  @Test
  public void findByIdThrowsException() {
    when(uebungRepository.findById(any())).thenReturn(Optional.empty());

    UebungService uebungService = new UebungService(uebungRepository);
    assertThrows(HttpClientErrorException.class,
        () -> {
          uebungService.findById(any());
        });
  }

  @Test
  public void abschliessenTest() {
    Uebung testUebung = new Uebung("TestUebung", Modus.GRUPPENANMELDUNG, 1, 4,
        LocalDateTime.now().minus(10, ChronoUnit.MINUTES),
        LocalDateTime.now().plus(10, ChronoUnit.MINUTES));
    when(uebungRepository.findById(1L)).thenReturn(Optional.of(testUebung));

    UebungService uebungService = new UebungService(uebungRepository);
    uebungService.abschliessen(1L);

    assertThat(testUebung.isBearbeitet()).isTrue();
    verify(uebungRepository,times(1)).save(testUebung);
  }

  @Test
  public void findByNameTest() {
    Uebung testUebung = new Uebung("TestUebung", Modus.GRUPPENANMELDUNG, 1, 4,
        LocalDateTime.now().minus(10, ChronoUnit.MINUTES),
        LocalDateTime.now().plus(10, ChronoUnit.MINUTES));
    when(uebungRepository.findByName("TestUebung")).thenReturn(Optional.of(testUebung));

    UebungService uebungService = new UebungService(uebungRepository);

    assertThat(uebungService.findByName("TestUebung")).isEqualTo(testUebung);
    verify(uebungRepository, times(1)).findByName("TestUebung");
  }

  @Test
  public void findByIdTest() {
    Uebung testUebung = new Uebung("TestUebung", Modus.GRUPPENANMELDUNG, 1, 4,
        LocalDateTime.now().minus(10, ChronoUnit.MINUTES),
        LocalDateTime.now().plus(10, ChronoUnit.MINUTES));
    when(uebungRepository.findById(1L)).thenReturn(Optional.of(testUebung));

    UebungService uebungService = new UebungService(uebungRepository);

    assertThat(uebungService.findById(1L)).isEqualTo(testUebung);
    verify(uebungRepository, times(1)).findById(1L);
  }

  @Test
  public void findByIdForStudentThrowsException(){
    // Der Anmeldungszeitraum dieser Uebung ist schon vorbei !
    Uebung testUebung = new Uebung("TestUebung", Modus.GRUPPENANMELDUNG, 1, 4,
        LocalDateTime.now().minus(10, ChronoUnit.MINUTES),
        LocalDateTime.now().minus(5, ChronoUnit.MINUTES));

    when(uebungRepository.findById(any())).thenReturn(Optional.of(testUebung));

    UebungService uebungService = new UebungService(uebungRepository);

    assertThrows(HttpClientErrorException.class,
        () -> {
          uebungService.findByIdForStudent(any());
        });
  }

  @Test
  public void findByIdForStudentTest(){
    // Der Anmeldungszeitraum dieser Uebung ist nicht vorbei !
    Uebung testUebung = new Uebung("TestUebung", Modus.GRUPPENANMELDUNG, 1, 4,
        LocalDateTime.now().minus(10, ChronoUnit.MINUTES),
        LocalDateTime.now().plus(5, ChronoUnit.MINUTES));

    when(uebungRepository.findById(any())).thenReturn(Optional.of(testUebung));
    UebungService uebungService = new UebungService(uebungRepository);

    assertThat(uebungService.findByIdForStudent(any())).isEqualTo(testUebung);
  }

  @Test
  public void findAllForStudentTest() {
    Uebung testUebung1 = new Uebung("TestUebung1", Modus.GRUPPENANMELDUNG, 1, 4,
        LocalDateTime.now().minus(10, ChronoUnit.MINUTES),
        LocalDateTime.now().minus(5, ChronoUnit.MINUTES));
    Uebung testUebung2 = new Uebung("TestUebung2", Modus.GRUPPENANMELDUNG, 1, 4,
        LocalDateTime.now().minus(10, ChronoUnit.MINUTES),
        LocalDateTime.now().plus(5, ChronoUnit.MINUTES));

    when(uebungRepository.findAll()).thenReturn(List.of(testUebung1, testUebung2));

    UebungService uebungService = new UebungService(uebungRepository);
    List<Uebung> results = uebungService.findAllForStudent();

    assertThat(results.size()).isEqualTo(1);
    assertThat(results.contains(testUebung1)).isFalse();
    assertThat(results.contains(testUebung2)).isTrue();
  }
}
