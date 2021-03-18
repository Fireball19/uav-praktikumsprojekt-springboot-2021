package de.hhu.propra.uav.domains.applicationservices;

import de.hhu.propra.uav.domains.model.student.Student;
import de.hhu.propra.uav.domains.model.uebung.Modus;
import de.hhu.propra.uav.domains.model.uebung.Uebung;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.web.client.HttpClientErrorException;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@ActiveProfiles("test")
public class VerwaltungServiceTests {

  @Mock
  StudentService studentService;
  @Mock
  UebungService uebungService;

  @Test
  public void addStudentThrowsExceptionOfNullTerminId(){
    VerwaltungService verwaltungService = new VerwaltungService(studentService, uebungService);
    Uebung uebung = new Uebung("TestUebung", Modus.GRUPPENANMELDUNG, 2, 3,
        LocalDateTime.now().minus(10, ChronoUnit.MINUTES),
        LocalDateTime.now().plus(10, ChronoUnit.MINUTES));

    when(uebungService.findById(any())).thenReturn(uebung);


    assertThrows(HttpClientErrorException.class,
        () -> {
          verwaltungService.addStudent("Alex",null,1L);
        });
  }

  @Test
  public void deleteStudentThrowsExceptionOfNullTerminId(){
    VerwaltungService verwaltungService = new VerwaltungService(studentService, uebungService);
    Uebung uebung = new Uebung("TestUebung", Modus.GRUPPENANMELDUNG, 2, 3,
        LocalDateTime.now().minus(10, ChronoUnit.MINUTES),
        LocalDateTime.now().plus(10, ChronoUnit.MINUTES));

    when(uebungService.findById(any())).thenReturn(uebung);


    assertThrows(HttpClientErrorException.class,
        () -> {
          verwaltungService.deleteStudent("Alex",null,1L);
        });
  }

  @Test
  public void moveStudentThrowsExceptionOfNullTerminId(){
    VerwaltungService verwaltungService = new VerwaltungService(studentService, uebungService);
    Uebung uebung = new Uebung("TestUebung", Modus.GRUPPENANMELDUNG, 2, 3,
        LocalDateTime.now().minus(10, ChronoUnit.MINUTES),
        LocalDateTime.now().plus(10, ChronoUnit.MINUTES));

    when(uebungService.findById(any())).thenReturn(uebung);


    assertThrows(HttpClientErrorException.class,
        () -> {
          verwaltungService.moveStudent("Alex",1L,null,null);
        });
  }


}
