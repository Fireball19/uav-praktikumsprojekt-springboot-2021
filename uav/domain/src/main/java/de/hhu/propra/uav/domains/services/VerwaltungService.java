package de.hhu.propra.uav.domains.services;

import de.hhu.propra.uav.domains.annotations.ApplicationService;
import de.hhu.propra.uav.domains.model.student.Student;
import de.hhu.propra.uav.domains.model.uebung.Uebung;
import org.springframework.http.HttpStatus;
import org.springframework.web.client.HttpClientErrorException;

@ApplicationService
public class VerwaltungService {

  private final StudentService studentService;
  private final UebungService uebungService;

  public VerwaltungService(StudentService studentService, UebungService uebungService) {
    this.studentService = studentService;
    this.uebungService = uebungService;
  }

  public void addStudent(final String github, final Long uebungId, final Long terminId) {
    Student student = studentService.findByGithub(github);
    Uebung uebung = uebungService.findById(uebungId);

    if (uebung.findTermin(terminId) == null) {
      throw new HttpClientErrorException(HttpStatus.NOT_FOUND,
          "Kein Termin mit id " + terminId + " vorhanden!");
    }

    if (uebung.containsStudent(student)) {
      throw new HttpClientErrorException(HttpStatus.CONFLICT,
          "Student " + github + " bereits vorhanden!");
    }

    uebung.addStudent(student, terminId);
    uebungService.save(uebung);
  }

  public void deleteStudent(final String github, final Long uebungId, final Long terminId) {
    Student student = studentService.findByGithub(github);
    Uebung uebung = uebungService.findById(uebungId);

    if (uebung.findTermin(terminId) == null) {
      throw new HttpClientErrorException(HttpStatus.NOT_FOUND,
          "Kein Termin mit id " + terminId + " vorhanden!");
    }

    if (!uebung.terminContainsStudent(terminId, student)) {
      throw new HttpClientErrorException(HttpStatus.NOT_FOUND,
          "Student " + github + " ist nicht vorhanden!");
    }

    uebung.deleteStudent(student, terminId);
    uebungService.save(uebung);
  }

  public void moveStudent(final String github, final Long uebungId, final Long terminAltId, final Long terminNeuId) {
    Student student = studentService.findByGithub(github);
    Uebung uebung = uebungService.findById(uebungId);

    if (uebung.findTermin(terminAltId) == null) {
      throw new HttpClientErrorException(HttpStatus.NOT_FOUND,
          "Kein Termin mit id " + terminAltId + " vorhanden!");
    }

    if (uebung.findTermin(terminNeuId) == null) {
      throw new HttpClientErrorException(HttpStatus.NOT_FOUND,
          "Kein Termin mit id " + terminNeuId + " vorhanden!");
    }

    if (!uebung.terminContainsStudent(terminAltId, student)) {
      throw new HttpClientErrorException(HttpStatus.NOT_FOUND,
          "Student " + github + " ist nicht vorhanden!");
    }

    uebung.moveStudent(student, terminAltId, terminNeuId);
    uebungService.save(uebung);
  }
}
