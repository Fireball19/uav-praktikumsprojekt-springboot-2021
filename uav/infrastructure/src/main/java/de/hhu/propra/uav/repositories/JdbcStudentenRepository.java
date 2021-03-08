package de.hhu.propra.uav.repositories;

import de.hhu.propra.uav.domains.student.Student;
import org.springframework.data.repository.CrudRepository;

import java.util.List;
import java.util.Optional;

public interface JdbcStudentenRepository extends CrudRepository<Student, String>, StudentRepository {

}
