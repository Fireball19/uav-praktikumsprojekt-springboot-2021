package de.hhu.propra.uav.domains.applicationservices;


import de.hhu.propra.uav.domains.annotations.ApplicationService;
import de.hhu.propra.uav.domains.model.student.Student;
import de.hhu.propra.uav.domains.model.student.StudentRepository;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;
import org.springframework.http.HttpStatus;
import org.springframework.web.client.HttpClientErrorException;


@SuppressWarnings("PMD.CommentDefaultAccessModifier")
@ApplicationService
public class StudentService {

  @SuppressWarnings("PMD.BeanMembersShouldSerialize")
  private final StudentRepository studentRepository;


  StudentService(final StudentRepository studentRepository) {
    this.studentRepository = studentRepository;
  }

  @SuppressWarnings("PMD.LawOfDemeter")
  public Map<Long, Student> findAllAsMap() {
    return studentRepository.findAll().stream()
        .collect(Collectors.toMap(Student::getId, Function.identity()));
  }

  public List<Student> findAll() {
    return studentRepository.findAll();
  }

  @SuppressWarnings("PMD.LawOfDemeter")
  public Student findById(final Long studentId) {
    return studentRepository.findById(studentId).orElseThrow(() ->
        new HttpClientErrorException(HttpStatus.NOT_FOUND,
            "Kein Student mit Id " + studentId + " vorhanden!"));
  }

  @SuppressWarnings("PMD.LawOfDemeter")
  public Student findByGithub(final String github) {
    return studentRepository.findByGithub(github).orElseThrow(() ->
        new HttpClientErrorException(HttpStatus.NOT_FOUND,
            "Kein Student mit Github Name " + github + " vorhanden!"));
  }

  public void addStudent(final String github) {
    final Student student = new Student(github);
    if (studentRepository.existsByGithub(student.getGithub())) {
      return;
    }
    studentRepository.save(student);
  }
}
