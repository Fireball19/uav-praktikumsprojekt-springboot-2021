package de.hhu.propra.uav.domains.services;

import de.hhu.propra.uav.domains.annotations.ApplicationService;
import de.hhu.propra.uav.domains.student.Student;
import de.hhu.propra.uav.repositories.StudentRepository;
import org.springframework.http.HttpStatus;
import org.springframework.web.client.HttpClientErrorException;

import java.util.List;

@ApplicationService
public class StudentService {

    private final StudentRepository studentRepository;

    StudentService(final StudentRepository studentRepository) {
        this.studentRepository = studentRepository;
    }

    public List<Student> findAll() {
        return studentRepository.findAll();
    }

    public Student findById(final Long id) {
        return studentRepository.findById(id).orElseThrow(() ->
                new HttpClientErrorException(HttpStatus.NOT_FOUND,"Kein Student mit Id " + id + " vorhanden!"));
    }

    public Student findByGithub(final String github) {
        return studentRepository.findByGithub(github).orElseThrow(() ->
                new HttpClientErrorException(HttpStatus.NOT_FOUND,
                        "Kein Student mit Github Name " + github + " vorhanden!"));
    }

    public void save(final Student student) {
      if(studentRepository.existsByGithub(student.getGithub())) {
        return;
      }
      studentRepository.save(student);
    }
}
