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
  }

}
