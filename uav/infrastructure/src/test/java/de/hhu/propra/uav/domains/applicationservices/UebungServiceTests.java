package de.hhu.propra.uav.domains.applicationservices;

import de.hhu.propra.uav.domains.github.GithubApi;
import de.hhu.propra.uav.domains.model.student.Student;
import de.hhu.propra.uav.domains.model.uebung.Modus;
import de.hhu.propra.uav.domains.model.uebung.Uebung;
import de.hhu.propra.uav.domains.model.uebung.UebungRepository;
import de.hhu.propra.uav.domains.model.uebung.VerteilungsService;
import de.hhu.propra.uav.domains.terminimporter.TerminFileDto;
import de.hhu.propra.uav.domains.terminimporter.TerminImporter;
import java.io.InputStream;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.context.ActiveProfiles;
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
@ActiveProfiles("test")
public class UebungServiceTests {

  @Mock
  UebungRepository uebungRepository;
  @Mock
  TerminImporter terminImporter;
  @Mock
  VerteilungsService verteilungsService;
  @Mock
  GithubApi githubApi;

  public Uebung getUebung()
      throws ClassNotFoundException, NoSuchMethodException, InvocationTargetException, IllegalAccessException {
    Uebung uebung = new Uebung("TestUebung", Modus.INDIVIDUALANMELDUNG, 2, 3,
        LocalDateTime.now().minus(10, ChronoUnit.MINUTES),
        LocalDateTime.now().plus(10, ChronoUnit.MINUTES));
    uebung.setId(1L);
    uebung.addTermin("Alex",LocalDateTime.now());
    uebung.addTermin("Bob",LocalDateTime.now());

    Class clazz = Class.forName("de.hhu.propra.uav.domains.model.uebung.Termin");
    Method method = clazz.getDeclaredMethod("setId", Long.class);
    method.setAccessible(true);

    method.invoke(uebung.getTermine().get(0),1L);
    method.invoke(uebung.getTermine().get(1),2L);

    return uebung;
  }

  public UebungService getUebungService(){
    return new UebungService(uebungRepository,terminImporter,verteilungsService,githubApi);
  }

  @Test
  public void createDefaultTest(){
    UebungService uebungService = getUebungService();
    Uebung uebung = uebungService.createDefault();

    assertThat(uebung.getName()).isEqualTo("DEFAULT");
  }

  @Test
  public void saveWithAlteTermineTest()
      throws ClassNotFoundException, NoSuchMethodException, IllegalAccessException, InvocationTargetException {
    UebungService uebungService = getUebungService();
    Uebung alteUebung = getUebung();

    when(uebungRepository.findTopByOrderByIdDesc()).thenReturn(alteUebung);
    uebungService.saveWithAlteTermine(alteUebung);

    verify(uebungRepository,times(1)).save(any());
  }

  @Test
  public void findFirstByBearbeitetIsFalseTest(){
    UebungService uebungService = getUebungService();
    uebungService.findFirstByBearbeitetIsFalse();

    verify(uebungRepository,times(1)).findFirstByBearbeitetIsFalse();
  }

  @Test
  public void findAllTest(){
    UebungService uebungService = getUebungService();
    List<Uebung> all = uebungService.findAll();

    verify(uebungRepository,times(1)).findAll();
  }

  @Test
  public void findByGithubThrowsException() {
    when(uebungRepository.findByName(anyString())).thenReturn(Optional.empty());

    UebungService uebungService = new UebungService(uebungRepository, terminImporter, verteilungsService, githubApi);
    assertThrows(HttpClientErrorException.class,
        () -> {
          uebungService.findByName("PU1");
        });
  }

  @Test
  public void findByIdThrowsException() {
    when(uebungRepository.findById(any())).thenReturn(Optional.empty());

    UebungService uebungService = new UebungService(uebungRepository, terminImporter, verteilungsService, githubApi);
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

    UebungService uebungService = new UebungService(uebungRepository, terminImporter, verteilungsService, githubApi);
    uebungService.abschliessen(1L);

    assertThat(testUebung.isBearbeitet()).isTrue();
    verify(uebungRepository, times(1)).save(testUebung);
  }

  @Test
  public void findByNameTest() {
    Uebung testUebung = new Uebung("TestUebung", Modus.GRUPPENANMELDUNG, 1, 4,
        LocalDateTime.now().minus(10, ChronoUnit.MINUTES),
        LocalDateTime.now().plus(10, ChronoUnit.MINUTES));
    when(uebungRepository.findByName("TestUebung")).thenReturn(Optional.of(testUebung));

    UebungService uebungService = new UebungService(uebungRepository, terminImporter, verteilungsService, githubApi);

    assertThat(uebungService.findByName("TestUebung")).isEqualTo(testUebung);
    verify(uebungRepository, times(1)).findByName("TestUebung");
  }

  @Test
  public void findByIdTest() {
    Uebung testUebung = new Uebung("TestUebung", Modus.GRUPPENANMELDUNG, 1, 4,
        LocalDateTime.now().minus(10, ChronoUnit.MINUTES),
        LocalDateTime.now().plus(10, ChronoUnit.MINUTES));
    when(uebungRepository.findById(1L)).thenReturn(Optional.of(testUebung));

    UebungService uebungService = new UebungService(uebungRepository, terminImporter, verteilungsService, githubApi);

    assertThat(uebungService.findById(1L)).isEqualTo(testUebung);
    verify(uebungRepository, times(1)).findById(1L);
  }

  @Test
  public void findByIdForStudentThrowsException() {
    // Der Anmeldungszeitraum dieser Uebung ist schon vorbei !
    Uebung testUebung = new Uebung("TestUebung", Modus.GRUPPENANMELDUNG, 1, 4,
        LocalDateTime.now().minus(10, ChronoUnit.MINUTES),
        LocalDateTime.now().minus(5, ChronoUnit.MINUTES));

    when(uebungRepository.findById(any())).thenReturn(Optional.of(testUebung));

    UebungService uebungService = new UebungService(uebungRepository, terminImporter, verteilungsService, githubApi);

    assertThrows(HttpClientErrorException.class,
        () -> {
          uebungService.findByIdForStudent(any());
        });
  }

  @Test
  public void findByIdForStudentTest() {
    // Der Anmeldungszeitraum dieser Uebung ist nicht vorbei !
    Uebung testUebung = new Uebung("TestUebung", Modus.GRUPPENANMELDUNG, 1, 4,
        LocalDateTime.now().minus(10, ChronoUnit.MINUTES),
        LocalDateTime.now().plus(5, ChronoUnit.MINUTES));

    when(uebungRepository.findById(any())).thenReturn(Optional.of(testUebung));
    UebungService uebungService = new UebungService(uebungRepository, terminImporter, verteilungsService, githubApi);

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

    UebungService uebungService = new UebungService(uebungRepository, terminImporter, verteilungsService, githubApi);
    List<Uebung> results = uebungService.findAllForStudent();

    assertThat(results.size()).isEqualTo(1);
    assertThat(results.contains(testUebung1)).isFalse();
    assertThat(results.contains(testUebung2)).isTrue();
  }

  @Test
  public void ueberpruefeMaxGroesseTest()
      throws ClassNotFoundException, NoSuchMethodException, IllegalAccessException, InvocationTargetException {
    UebungService uebungService = getUebungService();
    Uebung uebung = getUebung();

    when(uebungRepository.findById(1L)).thenReturn(Optional.ofNullable(uebung));

    assertThat(uebungService.ueberpruefeMaxGroesse(1L)).isEqualTo(3);
  }

  @Test
  public void ueberpruefeMinGroesseTest()
      throws ClassNotFoundException, NoSuchMethodException, IllegalAccessException, InvocationTargetException {
    UebungService uebungService = getUebungService();
    Uebung uebung = getUebung();

    when(uebungRepository.findById(1L)).thenReturn(Optional.ofNullable(uebung));

    assertThat(uebungService.ueberpruefeMinGroesse(1L)).isEqualTo(2);
  }


  @Test
  public void addGruppeTest()
      throws ClassNotFoundException, NoSuchMethodException, IllegalAccessException, InvocationTargetException {
    UebungService uebungService = getUebungService();
    Uebung uebung = getUebung();
    when(uebungRepository.findById(1L)).thenReturn(Optional.ofNullable(uebung));

    uebungService.addGruppe(1L,1L,"Gruppenname");

    verify(uebungRepository,times(1)).save(uebung);
  }

  @Test
  public void deleteGruppeTest()
      throws ClassNotFoundException, NoSuchMethodException, IllegalAccessException, InvocationTargetException {
    UebungService uebungService = getUebungService();
    Uebung uebung = getUebung();
    when(uebungRepository.findById(1L)).thenReturn(Optional.ofNullable(uebung));

    uebungService.deleteGruppe(1L,1L);

    verify(uebungRepository,times(1)).save(uebung);
  }

  @Test
  public void addTerminTest()
      throws ClassNotFoundException, NoSuchMethodException, IllegalAccessException, InvocationTargetException {
    UebungService uebungService = getUebungService();
    Uebung uebung = getUebung();
    when(uebungRepository.findById(1L)).thenReturn(Optional.ofNullable(uebung));

    uebungService.addTermin(1L,"Alex",LocalDateTime.now());

    verify(uebungRepository,times(1)).save(uebung);
  }

  @Test
  public void ueberpruefeAnmeldungsModusTest()
      throws ClassNotFoundException, NoSuchMethodException, IllegalAccessException, InvocationTargetException {
    UebungService uebungService = getUebungService();
    Uebung uebung = getUebung();
    when(uebungRepository.findById(1L)).thenReturn(Optional.ofNullable(uebung));

    assertThat(uebungService.ueberpruefeAnmeldungsModus(1L)).isEqualTo(Modus.INDIVIDUALANMELDUNG);
  }

  @Test
  public void addTermineByTerminImporterTest()
      throws ClassNotFoundException, NoSuchMethodException, IllegalAccessException, InvocationTargetException {
    UebungService uebungService = getUebungService();
    Uebung uebung = getUebung();
    when(uebungRepository.findById(1L)).thenReturn(Optional.ofNullable(uebung));
    LocalDateTime jetzt = LocalDateTime.now();
    when(terminImporter.convertToTerminFile(any())).thenReturn(List.of(new TerminFileDto("Alex",jetzt),
        new TerminFileDto("Bob",jetzt)));

    uebungService.addTermineByTerminImporter(1L, InputStream.nullInputStream());
    assertThat(uebung.getTermine().size()).isEqualTo(4);
  }

  @Test
  public void findTermineByStudentIdTest()
      throws ClassNotFoundException, NoSuchMethodException, IllegalAccessException, InvocationTargetException {
    UebungService uebungService = getUebungService();
    Uebung uebung1 = getUebung();
    Uebung uebung2 = getUebung();
    uebung2.setId(2L);

    uebung2.addStudent(new Student("student"),1L);

    when(uebungRepository.findAll()).thenReturn(List.of(uebung1,uebung2));

    List<Uebung> uebungen = uebungService.findTermineByStudentId(new Student("student"));

    assertThat(uebungen.size()).isEqualTo(2);
    assertThat(uebungen.get(0).getId()).isEqualTo(1L);
  }

  @Test
  public void deleteTerminTest()
      throws ClassNotFoundException, NoSuchMethodException, IllegalAccessException, InvocationTargetException {
    UebungService uebungService = getUebungService();
    Uebung uebung = getUebung();
    when(uebungRepository.findById(1L)).thenReturn(Optional.ofNullable(uebung));

    uebungService.deleteTermin(1L,1L);

    assertThat(uebung.getTermine().size()).isEqualTo(1);
  }

  @Test
  public void shuffleTutorenTest()
      throws ClassNotFoundException, NoSuchMethodException, IllegalAccessException, InvocationTargetException {
    UebungService uebungService = getUebungService();
    Uebung uebung = getUebung();
    when(uebungRepository.findById(1L)).thenReturn(Optional.ofNullable(uebung));

    uebungService.shuffleTutoren(1L);

    verify(verteilungsService,times(1)).tutorenVerteilen(uebung);
    verify(uebungRepository,times(1)).save(uebung);
  }

  @Test
  public void findAllIndividualAnmeldungTest(){
    UebungService uebungService = getUebungService();
    uebungService.findAllIndividualAnmeldung();

    verify(uebungRepository,times(1)).findAllByModusEquals(Modus.INDIVIDUALANMELDUNG);
  }

  @Test
  public void shuffleStudentenTest()
      throws ClassNotFoundException, NoSuchMethodException, IllegalAccessException, InvocationTargetException {
    UebungService uebungService = getUebungService();
    Uebung uebung = getUebung();
    when(uebungRepository.findById(1L)).thenReturn(Optional.ofNullable(uebung));

    uebungService.shuffleStudenten(1L);

    verify(verteilungsService,times(1)).perfekteVerteilung(uebung);
    verify(uebungRepository,times(1)).save(uebung);
  }

  @Test
  public void individualModusAbschliessenTest()
      throws Exception {
    UebungService uebungService = getUebungService();
    Uebung uebung = getUebung();
    uebung.addStudent(new Student("Alex"),1L);
    uebung.addStudent(new Student("Bob"),2L);
    when(uebungRepository.findById(1L)).thenReturn(Optional.ofNullable(uebung));

    uebungService.individualModusAbschliessen(1L);

    verify(githubApi,times(2)).createGithubRepositoryIndividualanmeldung(any(),any(),any(),any());
  }

}
