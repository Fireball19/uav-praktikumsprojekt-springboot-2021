package de.hhu.propra.uav.domains.model.student;

import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.data.relational.core.mapping.Table;

@SuppressWarnings("PMD")
@Data
@RequiredArgsConstructor
@Table("termin_student")
public class StudentRef {

  private final Long id;
}
