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

    StudentService(StudentRepository studentRepository) {
        this.studentRepository = studentRepository;
    }

    public List<Student> findAll() {
        return studentRepository.findAll();
    }

    public Student findById(Long id) {
        return studentRepository.findById(id).orElseThrow(() ->
                new HttpClientErrorException(HttpStatus.NOT_FOUND,"Kein Student mit Id " + id + " vorhanden!"));
    }

    public Student findByGithub(String github) {
        return studentRepository.findByGithub(github).orElseThrow(() ->
                new HttpClientErrorException(HttpStatus.NOT_FOUND,
                        "Kein Student mit Github Name " + github + " vorhanden!"));
    }

    public void save(Student student) {
        studentRepository.save(student);
    }
}
