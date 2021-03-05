package de.hhu.propra.uav.domains.services;

import de.hhu.propra.uav.domains.student.Student;
import org.springframework.data.repository.CrudRepository;

import java.util.List;
import java.util.Optional;

public interface StudentenRepository extends CrudRepository<Student, String> {

    List<Student> findAll();

    Optional<Student> findById(Long id);

    Optional<Student> findByGithub(String github);
}
