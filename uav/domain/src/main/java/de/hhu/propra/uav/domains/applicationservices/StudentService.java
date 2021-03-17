package de.hhu.propra.uav.domains.applicationservices;


import de.hhu.propra.uav.domains.annotations.ApplicationService;
import de.hhu.propra.uav.domains.model.student.Student;
import de.hhu.propra.uav.domains.model.student.StudentRepository;
import org.springframework.http.HttpStatus;
import org.springframework.web.client.HttpClientErrorException;

import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@ApplicationService
public class StudentService {

    @SuppressWarnings("PMD")
    private final StudentRepository studentRepository;

    @SuppressWarnings("PMD")
    StudentService(final StudentRepository studentRepository) {
        this.studentRepository = studentRepository;
    }

    public Map<Long,Student> findAllAsMap() {
      return studentRepository.findAll().stream()
          .collect(Collectors.toMap(Student::getId, Function.identity()));
    }

    public List<Student> findAll() {
      return studentRepository.findAll();
    }

  @SuppressWarnings("PMD")
    public Student findById(final Long id) {
        return studentRepository.findById(id).orElseThrow(() ->
                new HttpClientErrorException(HttpStatus.NOT_FOUND,"Kein Student mit Id " + id + " vorhanden!"));
    }

  @SuppressWarnings("PMD")
    public Student findByGithub(final String github) {
        return studentRepository.findByGithub(github).orElseThrow(() ->
                new HttpClientErrorException(HttpStatus.NOT_FOUND,
                        "Kein Student mit Github Name " + github + " vorhanden!"));
    }

    public void addStudent(final String github) {
      Student student = new Student(github);
      if(studentRepository.existsByGithub(student.getGithub())) {
        return;
      }
      studentRepository.save(student);
    }
}
