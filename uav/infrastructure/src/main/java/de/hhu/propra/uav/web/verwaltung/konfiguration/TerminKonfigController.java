package de.hhu.propra.uav.web.verwaltung.konfiguration;

import de.hhu.propra.uav.domains.applicationservices.StudentService;
import de.hhu.propra.uav.domains.applicationservices.UebungService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import java.time.LocalDateTime;

@SuppressWarnings({"PMD.AtLeastOneConstructor", "PMD.BeanMembersShouldSerialize", "PMD.AvoidDuplicateLiterals"})
@Controller
public class TerminKonfigController {

  @Autowired
  private UebungService uebungService;

  @Autowired
  private StudentService studentService;


  @Secured("ROLE_ORGA")
  @GetMapping("/verwaltung/konfiguration/termin")
  public String terminKonfiguration(final Model model) {
    model.addAttribute("id", 0L);
    model.addAttribute("uebungen", uebungService.findAll());
    return "verwaltung/termineKonfiguration";
  }

  @Secured("ROLE_ORGA")
  @GetMapping("/verwaltung/konfiguration/termin/{uebungId}")
  public String terminHinzufuegen(@PathVariable("uebungId") final Long uebungId, final Model model) {
    model.addAttribute("uebung", uebungService.findById(uebungId));
    model.addAttribute("studenten", studentService.findAllAsMap());
    return "verwaltung/uebungTermin";
  }

  @Secured("ROLE_ORGA")
  @PostMapping("/verwaltung/konfiguration/termin/{uebungId}/hinzufuegen")
  public String terminHinzufuegen(@PathVariable("uebungId") final Long uebungId, final String tutor,
                                  @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm") final LocalDateTime zeitpunkt) {
    uebungService.addTermin(uebungId, tutor, zeitpunkt);
    return "redirect:/verwaltung/konfiguration/termin/{uebungId}";
  }

  @Secured("ROLE_ORGA")
  @PostMapping("/verwaltung/konfiguration/termin/{uebungId}/{terminId}/entfernen")
  public String terminEntfernen(@PathVariable("uebungId") final Long uebungId, @PathVariable("terminId") final Long terminId) {
    uebungService.deleteTermin(uebungId, terminId);
    return "redirect:/verwaltung/konfiguration/termin/{uebungId}";
  }
}
