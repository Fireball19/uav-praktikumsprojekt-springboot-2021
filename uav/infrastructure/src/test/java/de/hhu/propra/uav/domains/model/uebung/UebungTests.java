package de.hhu.propra.uav.domains.model.uebung;

import de.hhu.propra.uav.domains.model.student.Student;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.test.context.ActiveProfiles;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ActiveProfiles("test")
public class UebungTests {

  private Uebung getUebung() {
    return new Uebung("Bill", Modus.GRUPPENANMELDUNG, 1, 5,
        LocalDateTime.of(2021, 1, 2, 12, 30),
        LocalDateTime.of(2021, 1, 5, 12, 30));
  }


  @Test
  public void addTerminTest() {
    Uebung uebung = getUebung();

    LocalDateTime zeitpunkt = LocalDateTime.of(2021, 1, 3, 12, 30);
    uebung.addTermin("Bill", zeitpunkt);
    assertThat(uebung.getTermine().size()).isEqualTo(1);
    assertThat(uebung.getTermine().get(0).getMaxGroesse()).isEqualTo(uebung.getMaxGroesse());
    assertThat(uebung.getTermine().get(0).getMinGroesse()).isEqualTo(uebung.getMinGroesse());
    assertThat(uebung.getTermine().get(0).isReserviert()).isEqualTo(false);
    assertThat(uebung.getTermine().get(0).getTutor()).isEqualTo("Bill");
    assertThat(uebung.getTermine().get(0).getZeitpunkt()).isEqualTo(zeitpunkt);
    assertThat(uebung.getTermine().get(0).getId()).isEqualTo(null);
  }


  @Test
  public void addStudentTest() {
    Student student = new Student("abc123");
    Uebung uebung = getUebung();

    uebung.addTermin("Bill", LocalDateTime.of(2021, 1, 3, 12, 30));
    uebung.getTermine().get(0).setId(1L);
    uebung.addStudent(student, 1L);

    assertThat(uebung.getTermine().get(0).getStudenten().size()).isEqualTo(1);

  }



  @Test
  public void deleteStudentTest() {
    Student student = new Student("abc123");
    Uebung uebung = getUebung();

    uebung.addTermin("Bill", LocalDateTime.of(2021, 1, 3, 12, 30));
    uebung.getTermine().get(0).setId(1L);
    uebung.addStudent(student, 1L);

    uebung.deleteStudent(student, 1L);


    assertThat(uebung.getTermine().get(0).getStudenten().size()).isEqualTo(0);

  }


  @Test
  public void getGruppenTest(){
    Uebung uebung = getUebung();
    uebung.addTermin("Alex",LocalDateTime.now());
    uebung.addTermin("Bob",LocalDateTime.now());
    uebung.addTermin("Charlie",LocalDateTime.now());

    assertThat(uebung.getGruppen().size()).isEqualTo(3);
  }

  @Test
  public void deleteTermin(){
    Uebung uebung = getUebung();
    uebung.addTermin("Alex",LocalDateTime.now());
    uebung.addTermin("Bob",LocalDateTime.now());
    uebung.addTermin("Charlie",LocalDateTime.now());
    uebung.getTermine().get(0).setId(1L);
    uebung.getTermine().get(1).setId(2L);
    uebung.getTermine().get(2).setId(3L);

    uebung.deleteTermin(1L);

    assertThat(uebung.getTermine().size()).isEqualTo(2);
  }

  @Test
  public void moveStudentTest(){
    Uebung uebung = getUebung();
    uebung.addTermin("Alex",LocalDateTime.now());
    uebung.addTermin("Bob",LocalDateTime.now());
    Termin termin1 = uebung.getTermine().get(0);
    termin1.setId(1L);
    Student student = new Student("Student");
    termin1.addStudent(student);
    Termin termin2 = uebung.getTermine().get(1);
    termin2.setId(2L);

    uebung.moveStudent(student,1L,2L);

    assertThat(termin1.getStudenten().size()).isEqualTo(0);
    assertThat(termin2.getStudenten().size()).isEqualTo(1);
  }

  @Test
  public void addGruppeTest(){
    Uebung uebung = getUebung();
    uebung.addTermin("Alex",LocalDateTime.now());
    uebung.getTermine().get(0).setId(1L);

    uebung.addGruppe("GruppenName",1L);

    assertThat(uebung.getGruppen().get(0).getGruppenname()).isEqualTo("GruppenName");

  }

  @Test
  public void deleteGruppeTest(){
    Uebung uebung = getUebung();
    uebung.addTermin("Alex",LocalDateTime.now());
    uebung.getTermine().get(0).setId(1L);

    uebung.addGruppe("GruppenName",1L);
    uebung.deleteGruppe(1L);

    assertThat(uebung.getGruppen().get(0).getGruppenname()).isEqualTo("");
  }

  @Test
  public void terminContainsStudentTest(){
    Uebung uebung = getUebung();
    uebung.addTermin("Alex",LocalDateTime.now());
    Termin termin = uebung.getTermine().get(0);
    termin.setId(1L);
    Student student = new Student("Student");
    termin.addStudent(student);

    boolean result = uebung.terminContainsStudent(1L, student);

    assertThat(result).isEqualTo(true);
  }

  @Test
  public void containsStudentTrueTest(){
    Uebung uebung = getUebung();
    uebung.addTermin("Alex",LocalDateTime.now());
    uebung.addTermin("Bob",LocalDateTime.now());
    Termin termin1 = uebung.getTermine().get(0);
    termin1.setId(1L);
    Termin termin2 = uebung.getTermine().get(1);
    termin2.setId(2L);
    Student student = new Student("Student");
    termin1.addStudent(student);

    boolean result = uebung.containsStudent(student);

    assertThat(result).isEqualTo(true);
  }

  @Test
  public void containsStudentFalseTest(){
    Uebung uebung = getUebung();
    uebung.addTermin("Alex",LocalDateTime.now());
    uebung.addTermin("Bob",LocalDateTime.now());
    Termin termin1 = uebung.getTermine().get(0);
    termin1.setId(1L);
    Termin termin2 = uebung.getTermine().get(1);
    termin2.setId(2L);
    Student student = new Student("Student");

    boolean result = uebung.containsStudent(student);

    assertThat(result).isEqualTo(false);
  }

  @Test
  public void getKapazitaetenTest(){
    LocalDateTime jetzt = LocalDateTime.now();
    Uebung uebung = getUebung();
    uebung.addTermin("Alex",jetzt);
    uebung.addTermin("Bob",jetzt);
    Termin termin1 = uebung.getTermine().get(0);
    termin1.setId(1L);
    Termin termin2 = uebung.getTermine().get(1);
    termin2.setId(2L);

    Student student = new Student("Student");
    termin1.addStudent(student);

    assertThat(uebung.getKapazitaeten().get(jetzt)).isEqualTo(2*uebung.getMaxGroesse()-1);
  }

  @Test
  public void hasTerminFreiePlaetzeTest(){
    Uebung uebung = getUebung();
    uebung.addTermin("Alex",LocalDateTime.now());
    Termin termin1 = uebung.getTermine().get(0);
    termin1.setId(1L);

    termin1.addStudent(new Student("s1"));
    termin1.addStudent(new Student("s2"));
    termin1.addStudent(new Student("s3"));
    termin1.addStudent(new Student("s4"));
    termin1.addStudent(new Student("s5"));

    boolean result = uebung.hasTerminFreiePlaetze(1L);

    assertThat(result).isEqualTo(false);
  }

  @Test
  public void filterTerminIdsByZeitpunktTest(){
    LocalDateTime jetzt = LocalDateTime.now();
    Uebung uebung = getUebung();
    uebung.addTermin("Alex",jetzt);
    uebung.addTermin("Bob",jetzt.plus(10, ChronoUnit.MINUTES));
    Termin termin1 = uebung.getTermine().get(0);
    termin1.setId(1L);
    Termin termin2 = uebung.getTermine().get(1);
    termin2.setId(2L);

    List<Long> longs1 = uebung.filterTerminIdsByZeitpunkt(jetzt);
    List<Long> longs2 = uebung.filterTerminIdsByZeitpunkt(jetzt.plus(10, ChronoUnit.MINUTES));

    assertThat(longs1.size()).isEqualTo(1);
    assertThat(longs1.get(0)).isEqualTo(1L);
    assertThat(longs2.size()).isEqualTo(1);
    assertThat(longs2.get(0)).isEqualTo(2L);
  }

  @Test
  public void filterTerminIdsByStudentTest(){
    LocalDateTime jetzt = LocalDateTime.now();
    Uebung uebung = getUebung();
    uebung.addTermin("Alex",jetzt);
    uebung.addTermin("Bob",jetzt);
    Termin termin1 = uebung.getTermine().get(0);
    termin1.setId(1L);
    Termin termin2 = uebung.getTermine().get(1);
    termin2.setId(2L);

    Student s1 = new Student("s1");
    termin1.addStudent(s1);

    List<Long> longs = uebung.filterTerminIdsByStudent(s1);

    assertThat(longs.size()).isEqualTo(1);
  }

  @Test
  public void getTerminDatenIndividualmodusTest(){
    LocalDateTime jetzt = LocalDateTime.now();
    Uebung uebung = getUebung();
    uebung.addTermin("Alex",jetzt);
    uebung.addTermin("Bob",jetzt);
    Termin termin1 = uebung.getTermine().get(0);
    termin1.setId(1L);
    Termin termin2 = uebung.getTermine().get(1);
    termin2.setId(2L);

    Student s1 = new Student("s1");
    termin1.addStudent(s1);

    assertThat(uebung.getTerminDatenIndividualmodus().size()).isEqualTo(1);
    TerminInfoDto terminInfoDto = uebung.getTerminDatenIndividualmodus().get(0);
    assertThat(terminInfoDto.getTutor()).isEqualTo("Alex");
    assertThat(terminInfoDto.getZeitpunkt()).isEqualTo(jetzt);
    assertThat(terminInfoDto.getStudenten().size()).isEqualTo(1);

  }
}
