package de.hhu.propra.uav;

import de.hhu.propra.uav.domains.Modus;
import de.hhu.propra.uav.domains.Student;
import de.hhu.propra.uav.domains.Uebung;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.time.LocalDateTime;

@SuppressWarnings("PMD")
@SpringBootApplication
public class UebungsUndAnmeldungsverwaltungApplication {

  public static void main(String[] args) {
    SpringApplication.run(UebungsUndAnmeldungsverwaltungApplication.class, args);

    Uebung uebung1 = new Uebung("PÜ", Modus.GRUPPENANMELDUNG,
        1, 4, LocalDateTime.MIN, LocalDateTime.MIN);

    uebung1.terminHinzufuegen("Alex", LocalDateTime.MIN);

    uebung1.terminHinzufuegen("Dieter", LocalDateTime.MAX);

    Student student = new Student("test");

    uebung1.addStudent(student, LocalDateTime.MIN, "Alex");

    uebung1.moveStudent(student, LocalDateTime.MIN, "Alex", LocalDateTime.MAX, "Dieter");

    //uebung1.getTermine().forEach(x -> System.out.println(x.getTutor() + x.getZeitpunkt() + x.getStudenten().get(0).getGithub()));
  }

}
