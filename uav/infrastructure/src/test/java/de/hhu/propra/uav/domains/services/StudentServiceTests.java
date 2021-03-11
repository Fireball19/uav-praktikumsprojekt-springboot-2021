package de.hhu.propra.uav.domains.services;

import de.hhu.propra.uav.domains.model.student.Student;
import de.hhu.propra.uav.domains.model.student.StudentRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
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

  @Test()
  public void throwException() {
    when(studentRepository.findByGithub(anyString())).thenReturn(Optional.empty());

    StudentService studentService = new StudentService(studentRepository);
    studentService.findByGithub("Alex");


  }
}
