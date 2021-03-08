package de.hhu.propra.uav.domains;

import de.hhu.propra.uav.domains.student.Student;
import de.hhu.propra.uav.domains.uebung.Modus;
import de.hhu.propra.uav.domains.uebung.Uebung;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;


public class UebungTests {

    private Uebung getUebung() {
      return new Uebung("Bill", Modus.GRUPPENANMELDUNG,1,5,
              LocalDateTime.of(2021,1,2,12,30),
              LocalDateTime.of(2021,1,5,12,30));
    }


    @Test
    public void addTerminTest(){
        Uebung uebung = getUebung();

        uebung.addTermin("Bill",LocalDateTime.of(2021,1,3,12,30));
        assertThat(uebung.getTermine().size()).isEqualTo(1);
    }



    @Test
    public void addStudentTest(){
        Student student = new Student("abc123");
        Uebung uebung = getUebung();

        uebung.addTermin("Bill",LocalDateTime.of(2021,1,3,12,30));
        uebung.getTermine().get(0).setId(1L);
        uebung.addStudent(student,1L);

        assertThat(uebung.getTermine().get(0).getStudenten().size()).isEqualTo(1);

    }

    @Test
    public void deleteStudentTest(){
        Student student = new Student("abc123");
        Uebung uebung = getUebung();

        uebung.addTermin("Bill",LocalDateTime.of(2021,1,3,12,30));
        uebung.getTermine().get(0).setId(1L);
        uebung.addStudent(student,1L);

        uebung.deleteStudent(student,1L);


        assertThat(uebung.getTermine().get(0).getStudenten().size()).isEqualTo(0);

    }

}
