package de.hhu.propra.uav.domains.applicationservices;

import de.hhu.propra.uav.domains.model.student.Student;
import de.hhu.propra.uav.domains.model.student.StudentRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.web.client.HttpClientErrorException;

import java.util.Arrays;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@ActiveProfiles("test")
public class StudentServiceTests {

  @Mock
  StudentRepository studentRepository;

  @Test
  public void addStudentAlreadyExists() {
    when(studentRepository.existsByGithub("Alex")).thenReturn(true);

    StudentService studentService = new StudentService(studentRepository);
    studentService.addStudent("Alex");

    verify(studentRepository, never()).save(any());
  }

  @Test
  public void addStudentTest() {
    when(studentRepository.existsByGithub("Alex")).thenReturn(false);

    StudentService studentService = new StudentService(studentRepository);
    studentService.addStudent("Alex");

    verify(studentRepository, times(1)).save(new Student("Alex"));
  }

  @Test
  public void findByGithubThrowsException() {
    when(studentRepository.findByGithub(anyString())).thenReturn(Optional.empty());

    StudentService studentService = new StudentService(studentRepository);
    assertThrows(HttpClientErrorException.class,
        () -> {
          studentService.findByGithub("Alex");
        });
  }

  @Test
  public void findByIdThrowsException() {
    when(studentRepository.findById(any())).thenReturn(Optional.empty());

    StudentService studentService = new StudentService(studentRepository);
    assertThrows(HttpClientErrorException.class,
        () -> {
          studentService.findById(any());
        });
  }

  @Test
  public void findByGithubTest() {
    when(studentRepository.findByGithub("Alex")).thenReturn(Optional.of(new Student("Alex")));

    StudentService studentService = new StudentService(studentRepository);

    assertThat(studentService.findByGithub("Alex")).isEqualTo(new Student("Alex"));
    verify(studentRepository, times(1)).findByGithub("Alex");
  }

  @Test
  public void findByIdTest() {
    when(studentRepository.findById(1L)).thenReturn(Optional.of(new Student("Alex")));

    StudentService studentService = new StudentService(studentRepository);

    assertThat(studentService.findById(1L)).isEqualTo(new Student("Alex"));
    verify(studentRepository, times(1)).findById(1L);
  }

  @Test
  public void findAllAsMapTest() {
    Student alex = new Student("Alex");
    alex.setId(1L);
    Student bob = new Student("Bob");
    bob.setId(2L);
    Student charlie = new Student("Charlie");
    charlie.setId(3L);

    when(studentRepository.findAll()).thenReturn(Arrays.asList(alex, bob, charlie));

    StudentService studentService = new StudentService(studentRepository);

    assertThat(studentService.findAllAsMap().containsValue(alex)).isEqualTo(true);
    assertThat(studentService.findAllAsMap().containsValue(bob)).isEqualTo(true);
    assertThat(studentService.findAllAsMap().containsValue(charlie)).isEqualTo(true);
    assertThat(studentService.findAllAsMap().containsValue(new Student("David"))).isEqualTo(false);


  }
}
