package de.hhu.propra.uav.repositories;

import de.hhu.propra.uav.domains.model.student.Student;
import de.hhu.propra.uav.domains.model.student.StudentRepository;
import org.springframework.data.repository.CrudRepository;

public interface JdbcStudentenRepository extends CrudRepository<Student, String>, StudentRepository {

}
