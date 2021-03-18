package de.hhu.propra.uav.domains.model.student;

import java.util.List;
import java.util.Optional;

public interface StudentRepository {

  List<Student> findAll();

  Optional<Student> findById(Long studentId);

  Optional<Student> findByGithub(String github);

  Student save(Student student);

  boolean existsByGithub(String github);
}
