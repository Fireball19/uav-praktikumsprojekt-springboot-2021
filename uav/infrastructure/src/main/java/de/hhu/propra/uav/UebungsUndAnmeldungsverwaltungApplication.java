package de.hhu.propra.uav;

import de.hhu.propra.uav.domains.annotations.ApplicationService;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

import static org.springframework.context.annotation.FilterType.ANNOTATION;

@SuppressWarnings("PMD")
@SpringBootApplication
@ComponentScan(
        includeFilters = {
                @ComponentScan.Filter(type = ANNOTATION, classes = ApplicationService.class)
        }
)
public class UebungsUndAnmeldungsverwaltungApplication {

  public static void main(String[] args) throws Exception {
    SpringApplication.run(UebungsUndAnmeldungsverwaltungApplication.class, args);
  }

}
