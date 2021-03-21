package de.hhu.propra.uav.domains.model.uebung;

import static org.assertj.core.api.Assertions.assertThat;

import de.hhu.propra.uav.domains.model.student.Student;
import java.time.LocalDateTime;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;

@ActiveProfiles("test")
public class VerteilungsServiceTests {

  private VerteilungsService verteilungsService = new VerteilungsService();


  private Uebung getUebung() {
    Uebung uebung = new Uebung("TestUebung", Modus.INDIVIDUALANMELDUNG, 1, 2,
        LocalDateTime.of(2021, 1, 2, 12, 30),
        LocalDateTime.of(2021, 1, 5, 12, 30));

    LocalDateTime jetzt = LocalDateTime.now();
    uebung.addTermin("Alex", jetzt);
    uebung.addTermin("Bob", jetzt);
    uebung.addTermin("Charlie",jetzt);
    uebung.addTermin("David",jetzt);
    Termin termin1 = uebung.getTermine().get(0);
    termin1.setId(1L);
    termin1.addStudent(new Student("101"));
    termin1.addStudent(new Student("102"));
    Termin termin2 = uebung.getTermine().get(1);
    termin2.setId(2L);
    termin2.addStudent(new Student("201"));
    termin2.addStudent(new Student("202"));
    Termin termin3 = uebung.getTermine().get(2);
    termin3.setId(3L);
    termin3.addStudent(new Student("301"));
    Termin termin4 = uebung.getTermine().get(3);
    termin4.setId(4L);
    termin4.addStudent(new Student("401"));

    return uebung;
  }

  private Uebung getAnotherUebung() {
    Uebung uebung = new Uebung("TestUebung", Modus.INDIVIDUALANMELDUNG, 1, 2,
        LocalDateTime.of(2021, 1, 2, 12, 30),
        LocalDateTime.of(2021, 1, 5, 12, 30));

    LocalDateTime jetzt = LocalDateTime.now();
    uebung.addTermin("Alex", jetzt);
    uebung.addTermin("Bob", jetzt);
    uebung.addTermin("Charlie",jetzt);
    uebung.addTermin("David",jetzt);
    Termin termin1 = uebung.getTermine().get(0);
    termin1.setId(1L);
    termin1.addStudent(new Student("101"));
    termin1.addStudent(new Student("102"));
    Termin termin2 = uebung.getTermine().get(1);
    termin2.setId(2L);
    termin2.addStudent(new Student("201"));
    termin2.addStudent(new Student("202"));
    Termin termin3 = uebung.getTermine().get(2);
    termin3.setId(3L);
    termin3.addStudent(new Student("301"));
    Termin termin4 = uebung.getTermine().get(3);
    termin4.setId(4L);

    return uebung;
  }

  private Uebung getUebungWithoutStudents() {
    Uebung uebung = new Uebung("TestUebung", Modus.INDIVIDUALANMELDUNG, 1, 2,
        LocalDateTime.of(2021, 1, 2, 12, 30),
        LocalDateTime.of(2021, 1, 5, 12, 30));

    LocalDateTime jetzt = LocalDateTime.now();
    uebung.addTermin("Alex", jetzt);
    uebung.addTermin("Bob", jetzt);
    uebung.addTermin("Charlie",jetzt);
    uebung.addTermin("David",jetzt);
    Termin termin1 = uebung.getTermine().get(0);
    termin1.setId(1L);
    Termin termin2 = uebung.getTermine().get(1);
    termin2.setId(2L);
    Termin termin3 = uebung.getTermine().get(2);
    termin3.setId(3L);
    Termin termin4 = uebung.getTermine().get(3);
    termin4.setId(4L);

    return uebung;
  }

  @Test
  public void tutorenVerteilenTest() {
    Uebung uebung = getUebung();

    verteilungsService.tutorenVerteilen(uebung);

    List<Termin> termine = uebung.getTermine();
    boolean result = termine.get(0).getTutor().equals("Alex")
                  && termine.get(1).getTutor().equals("Bob")
                  && termine.get(2).getTutor().equals("Charlie")
                  && termine.get(3).getTutor().equals("David");
    assertThat(result).isEqualTo(false);
  }

  @Test
  public void perfekteVerteilungTest1(){
    Uebung uebung = getUebung();

    verteilungsService.perfekteVerteilung(uebung);

    List<Termin> termine = uebung.getTermine();
    boolean result = termine.get(0).getStudenten().size() == 0
        || termine.get(1).getStudenten().size() == 0
        || termine.get(2).getStudenten().size() == 0
        || termine.get(3).getStudenten().size() == 0;
    assertThat(result).isEqualTo(true);
  }

  @Test
  public void perfekteVerteilungTest2(){
    Uebung uebung = getAnotherUebung();

    verteilungsService.perfekteVerteilung(uebung);

    List<Termin> termine = uebung.getTermine();
    boolean result1 = termine.get(0).getStudenten().size() == 1
        || termine.get(1).getStudenten().size() == 1
        || termine.get(2).getStudenten().size() == 1
        || termine.get(3).getStudenten().size() == 1;

    boolean result2 = termine.get(0).getStudenten().size() == 0
        || termine.get(1).getStudenten().size() == 0
        || termine.get(2).getStudenten().size() == 0
        || termine.get(3).getStudenten().size() == 0;
    assertThat(result1 && result2).isEqualTo(true);
  }



  @Test
  public void perfekteVerteilungTest3(){
    Uebung uebung = getUebungWithoutStudents();

    verteilungsService.perfekteVerteilung(uebung);

    List<Termin> termine = uebung.getTermine();
    boolean result = termine.get(0).getStudenten().size() == 0
        && termine.get(1).getStudenten().size() == 0
        && termine.get(2).getStudenten().size() == 0
        && termine.get(3).getStudenten().size() == 0;
    assertThat(result).isEqualTo(true);
  }

}
