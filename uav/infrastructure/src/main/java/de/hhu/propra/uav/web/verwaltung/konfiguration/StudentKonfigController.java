package de.hhu.propra.uav.web.verwaltung.konfiguration;

import de.hhu.propra.uav.domains.applicationservices.StudentService;
import de.hhu.propra.uav.domains.applicationservices.UebungService;
import de.hhu.propra.uav.domains.applicationservices.VerwaltungService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

@SuppressWarnings({"PMD.AtLeastOneConstructor", "PMD.BeanMembersShouldSerialize", "PMD.AvoidDuplicateLiterals"})
@Controller
public class StudentKonfigController {

  @Autowired
  private UebungService uebungService;
  @Autowired
  private StudentService studentService;
  @Autowired
  private VerwaltungService verwaltungService;

  @Secured("ROLE_ORGA")
  @GetMapping("/verwaltung/konfiguration/studenten")
  public String studentenVerwaltung(final Model model) {
    model.addAttribute("studenten", studentService.findAll());
    model.addAttribute("uebungen", uebungService.findAll());

    return "verwaltung/studentenKonfiguration";
  }

  @Secured("ROLE_ORGA")
  @PostMapping("/verwaltung/konfiguration/studenten")
  public String studentKonfigurieren(final String github) {
    studentService.addStudent(github);

    return "redirect:/verwaltung/konfiguration/studenten";
  }

  @Secured("ROLE_ORGA")
  @GetMapping("/verwaltung/konfiguration/studenten/{uebungId}")
  public String studentenVerwaltung(final Model model, @PathVariable("uebungId") final Long uebungId) {
    model.addAttribute("uebung", uebungService.findById(uebungId));
    model.addAttribute("studenten", studentService.findAllAsMap());

    return "verwaltung/studentenTermin";
  }

  @Secured("ROLE_ORGA")
  @PostMapping("/verwaltung/konfiguration/studenten/{uebungId}/hinzufuegen")
  public String studentenTerminHinzufuegen(@PathVariable("uebungId") final Long uebungId, final String github, final Long terminId) {
    verwaltungService.addStudent(github, uebungId, terminId);
    return "redirect:/verwaltung/konfiguration/studenten/{uebungId}";
  }

  @Secured("ROLE_ORGA")
  @PostMapping("/verwaltung/konfiguration/studenten/{uebungId}/entfernen")
  public String studentenTerminEntfernen(@PathVariable("uebungId") final Long uebungId, final String github, final Long terminId) {
    verwaltungService.deleteStudent(github, uebungId, terminId);
    return "redirect:/verwaltung/konfiguration/studenten/{uebungId}";
  }

  @Secured("ROLE_ORGA")
  @PostMapping("/verwaltung/konfiguration/studenten/{uebungId}/verschieben")
  public String studentenTerminVerschieben(@PathVariable("uebungId") final Long uebungId,
                                           final String github, final Long terminAltId, final Long terminNeuId) {
    verwaltungService.moveStudent(github, uebungId, terminAltId, terminNeuId);
    return "redirect:/verwaltung/konfiguration/studenten/{uebungId}";
  }
}
