package de.hhu.propra.uav.domains.applicationservices;

import de.hhu.propra.uav.domains.annotations.ApplicationService;
import de.hhu.propra.uav.domains.model.student.Student;
import de.hhu.propra.uav.domains.model.uebung.Uebung;
import org.springframework.http.HttpStatus;
import org.springframework.web.client.HttpClientErrorException;

@SuppressWarnings("PMD.AvoidDuplicateLiterals")
@ApplicationService
public class VerwaltungService {

  @SuppressWarnings("PMD.BeanMembersShouldSerialize")
  private final StudentService studentService;

  @SuppressWarnings("PMD.BeanMembersShouldSerialize")
  private final UebungService uebungService;

  public VerwaltungService(final StudentService studentService, final UebungService uebungService) {
    this.studentService = studentService;
    this.uebungService = uebungService;
  }

  @SuppressWarnings("PMD.LawOfDemeter")
  public void addStudent(final String github, final Long uebungId, final Long terminId) {
    final Uebung uebung = uebungService.findById(uebungId);

    if (uebung.findTermin(terminId) == null) {
      throw new HttpClientErrorException(HttpStatus.NOT_FOUND,
          "Kein Termin mit id " + terminId + " vorhanden!");
    }

    final Student student = studentService.findByGithub(github);

    if (uebung.containsStudent(student)) {
      throw new HttpClientErrorException(HttpStatus.CONFLICT,
          "Student " + github + " bereits vorhanden!");
    }

    uebung.addStudent(student, terminId);
    uebungService.save(uebung);
  }

  @SuppressWarnings("PMD.LawOfDemeter")
  public void deleteStudent(final String github, final Long uebungId, final Long terminId) {
    final Uebung uebung = uebungService.findById(uebungId);

    if (uebung.findTermin(terminId) == null) {
      throw new HttpClientErrorException(HttpStatus.NOT_FOUND,
          "Kein Termin mit id " + terminId + " vorhanden!");
    }

    final Student student = studentService.findByGithub(github);

    if (!uebung.terminContainsStudent(terminId, student)) {
      throw new HttpClientErrorException(HttpStatus.NOT_FOUND,
          "Student " + github + " ist nicht vorhanden!");
    }

    uebung.deleteStudent(student, terminId);
    uebungService.save(uebung);
  }

  @SuppressWarnings("PMD.LawOfDemeter")
  public void moveStudent(final String github, final Long uebungId, final Long terminAltId, final Long terminNeuId) {
    final Uebung uebung = uebungService.findById(uebungId);

    if (uebung.findTermin(terminAltId) == null) {
      throw new HttpClientErrorException(HttpStatus.NOT_FOUND,
          "Kein Termin mit id " + terminAltId + " vorhanden!");
    }

    if (uebung.findTermin(terminNeuId) == null) {
      throw new HttpClientErrorException(HttpStatus.NOT_FOUND,
          "Kein Termin mit id " + terminNeuId + " vorhanden!");
    }

    final Student student = studentService.findByGithub(github);

    if (!uebung.terminContainsStudent(terminAltId, student)) {
      throw new HttpClientErrorException(HttpStatus.NOT_FOUND,
          "Student " + github + " ist nicht vorhanden!");
    }

    uebung.moveStudent(student, terminAltId, terminNeuId);
    uebungService.save(uebung);
  }
}
