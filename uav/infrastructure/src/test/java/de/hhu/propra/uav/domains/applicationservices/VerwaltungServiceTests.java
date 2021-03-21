package de.hhu.propra.uav.domains.applicationservices;

import de.hhu.propra.uav.domains.model.student.Student;
import de.hhu.propra.uav.domains.model.uebung.Modus;
import de.hhu.propra.uav.domains.model.uebung.Uebung;
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

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@ActiveProfiles("test")
public class VerwaltungServiceTests {

  @Mock
  StudentService studentService;
  @Mock
  UebungService uebungService;

  @Test
  public void addStudentThrowsException1() {
    VerwaltungService verwaltungService = new VerwaltungService(studentService, uebungService);
    Uebung uebung = new Uebung("TestUebung", Modus.GRUPPENANMELDUNG, 2, 3,
        LocalDateTime.now().minus(10, ChronoUnit.MINUTES),
        LocalDateTime.now().plus(10, ChronoUnit.MINUTES));

    when(uebungService.findById(any())).thenReturn(uebung);


    assertThrows(HttpClientErrorException.class,
        () -> {
          verwaltungService.addStudent("Tutor", null, 1L);
        });
  }

  @Test
  public void addStudentThrowsException2()
      throws ClassNotFoundException, NoSuchMethodException, InvocationTargetException, IllegalAccessException {
    VerwaltungService verwaltungService = new VerwaltungService(studentService, uebungService);
    Uebung uebung = new Uebung("TestUebung", Modus.GRUPPENANMELDUNG, 2, 3,
        LocalDateTime.now().minus(10, ChronoUnit.MINUTES),
        LocalDateTime.now().plus(10, ChronoUnit.MINUTES));
    uebung.setId(1L);
    uebung.addTermin("Tutor",LocalDateTime.now());

    Class clazz = Class.forName("de.hhu.propra.uav.domains.model.uebung.Termin");
    Method method = clazz.getDeclaredMethod("setId", Long.class);
    method.setAccessible(true);

    method.invoke(uebung.getTermine().get(0),1L);

    Student student = new Student("student");
    uebung.addStudent(student,1L);

    when(uebungService.findById(any())).thenReturn(uebung);
    when(studentService.findByGithub(any())).thenReturn(student);

    assertThrows(HttpClientErrorException.class,
        () -> {
          verwaltungService.addStudent("student", 1L, 1L);
        });
  }

  @Test
  public void addStudentTest()
      throws ClassNotFoundException, NoSuchMethodException, InvocationTargetException, IllegalAccessException {
    VerwaltungService verwaltungService = new VerwaltungService(studentService, uebungService);
    Uebung uebung = new Uebung("TestUebung", Modus.GRUPPENANMELDUNG, 2, 3,
        LocalDateTime.now().minus(10, ChronoUnit.MINUTES),
        LocalDateTime.now().plus(10, ChronoUnit.MINUTES));
    uebung.setId(1L);
    uebung.addTermin("Tutor",LocalDateTime.now());

    Class clazz = Class.forName("de.hhu.propra.uav.domains.model.uebung.Termin");
    Method method = clazz.getDeclaredMethod("setId", Long.class);
    method.setAccessible(true);

    method.invoke(uebung.getTermine().get(0),1L);

    when(uebungService.findById(any())).thenReturn(uebung);
    Student student = new Student("student");
    when(studentService.findByGithub(any())).thenReturn(student);

    verwaltungService.addStudent("Alex",1L,1L);

    verify(uebungService,times(1)).save(uebung);


  }

  @Test
  public void deleteStudentThrowsException1() {
    VerwaltungService verwaltungService = new VerwaltungService(studentService, uebungService);
    Uebung uebung = new Uebung("TestUebung", Modus.GRUPPENANMELDUNG, 2, 3,
        LocalDateTime.now().minus(10, ChronoUnit.MINUTES),
        LocalDateTime.now().plus(10, ChronoUnit.MINUTES));

    when(uebungService.findById(any())).thenReturn(uebung);


    assertThrows(HttpClientErrorException.class,
        () -> {
          verwaltungService.deleteStudent("Alex", null, 1L);
        });
  }

  @Test
  public void deleteStudentThrowsException2()
      throws ClassNotFoundException, NoSuchMethodException, InvocationTargetException, IllegalAccessException {
    VerwaltungService verwaltungService = new VerwaltungService(studentService, uebungService);
    Uebung uebung = new Uebung("TestUebung", Modus.GRUPPENANMELDUNG, 2, 3,
        LocalDateTime.now().minus(10, ChronoUnit.MINUTES),
        LocalDateTime.now().plus(10, ChronoUnit.MINUTES));
    uebung.setId(1L);
    uebung.addTermin("Alex",LocalDateTime.now());

    Class clazz = Class.forName("de.hhu.propra.uav.domains.model.uebung.Termin");
    Method method = clazz.getDeclaredMethod("setId", Long.class);
    method.setAccessible(true);

    method.invoke(uebung.getTermine().get(0),1L);

    Student student = new Student("student");
    when(uebungService.findById(any())).thenReturn(uebung);
    when(studentService.findByGithub(any())).thenReturn(student);


    assertThrows(HttpClientErrorException.class,
        () -> {
          verwaltungService.deleteStudent("Alex", 1L, 1L);
        });
  }

  @Test
  public void deleteStudentTest()
      throws ClassNotFoundException, NoSuchMethodException, InvocationTargetException, IllegalAccessException {
    VerwaltungService verwaltungService = new VerwaltungService(studentService, uebungService);
    Uebung uebung = new Uebung("TestUebung", Modus.GRUPPENANMELDUNG, 2, 3,
        LocalDateTime.now().minus(10, ChronoUnit.MINUTES),
        LocalDateTime.now().plus(10, ChronoUnit.MINUTES));
    uebung.setId(1L);
    uebung.addTermin("Alex",LocalDateTime.now());

    Class clazz = Class.forName("de.hhu.propra.uav.domains.model.uebung.Termin");
    Method method = clazz.getDeclaredMethod("setId", Long.class);
    method.setAccessible(true);

    method.invoke(uebung.getTermine().get(0),1L);

    Student student = new Student("student");
    uebung.addStudent(student,1L);

    when(uebungService.findById(any())).thenReturn(uebung);
    when(studentService.findByGithub(any())).thenReturn(student);

    verwaltungService.deleteStudent("student",1L,1L);

    verify(uebungService,times(1)).save(uebung);
  }

  @Test
  public void moveStudentThrowsException1()
      throws ClassNotFoundException, NoSuchMethodException, InvocationTargetException, IllegalAccessException {
    VerwaltungService verwaltungService = new VerwaltungService(studentService, uebungService);
    Uebung uebung = new Uebung("TestUebung", Modus.GRUPPENANMELDUNG, 2, 3,
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

    Student student = new Student("student");
    uebung.addStudent(student,1L);

    when(uebungService.findById(any())).thenReturn(uebung);

    assertThrows(HttpClientErrorException.class,
        () -> {
          verwaltungService.moveStudent("Alex", 1L, 3L, 2L);
        });
  }

  @Test
  public void moveStudentThrowsException2()
      throws ClassNotFoundException, NoSuchMethodException, InvocationTargetException, IllegalAccessException {
    VerwaltungService verwaltungService = new VerwaltungService(studentService, uebungService);
    Uebung uebung = new Uebung("TestUebung", Modus.GRUPPENANMELDUNG, 2, 3,
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

    Student student = new Student("student");
    uebung.addStudent(student,1L);

    when(uebungService.findById(any())).thenReturn(uebung);

    assertThrows(HttpClientErrorException.class,
        () -> {
          verwaltungService.moveStudent("Alex", 1L, 1L, 3L);
        });
  }

  @Test
  public void moveStudentThrowsException3()
      throws ClassNotFoundException, NoSuchMethodException, InvocationTargetException, IllegalAccessException {
    VerwaltungService verwaltungService = new VerwaltungService(studentService, uebungService);
    Uebung uebung = new Uebung("TestUebung", Modus.GRUPPENANMELDUNG, 2, 3,
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

    Student student = new Student("student");

    when(uebungService.findById(any())).thenReturn(uebung);
    when(studentService.findByGithub(any())).thenReturn(student);


    assertThrows(HttpClientErrorException.class,
        () -> {
          verwaltungService.moveStudent("student", 1L, 1L, 2L);
        });
  }

  @Test
  public void moveStudentTest()
      throws ClassNotFoundException, NoSuchMethodException, InvocationTargetException, IllegalAccessException {
    VerwaltungService verwaltungService = new VerwaltungService(studentService, uebungService);
    Uebung uebung = new Uebung("TestUebung", Modus.GRUPPENANMELDUNG, 2, 3,
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

    Student student = new Student("student");
    uebung.addStudent(student,1L);

    when(uebungService.findById(any())).thenReturn(uebung);
    when(studentService.findByGithub(any())).thenReturn(student);

    verwaltungService.moveStudent("student",1L,1L,2L);
    verify(uebungService,times(1)).save(uebung);
  }


}
