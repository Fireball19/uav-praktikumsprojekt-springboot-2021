package de.hhu.propra.uav.domains.repositories;

import de.hhu.propra.uav.domains.model.student.Student;

import java.util.List;
import java.util.Optional;

@SuppressWarnings("PMD")
public interface StudentRepository {

  List<Student> findAll();

  Optional<Student> findById(Long id);

  Optional<Student> findByGithub(String github);

  Student save (Student student);

  boolean existsByGithub(String github);
}
