package de.hhu.propra.uav.repositories;

import de.hhu.propra.uav.domains.student.Student;

import java.util.List;
import java.util.Optional;

public interface StudentRepository {

  List<Student> findAll();

  Optional<Student> findById(Long id);

  Optional<Student> findByGithub(String github);
}
