package de.hhu.propra.uav.web.verwaltung.uebersicht;

import de.hhu.propra.uav.domains.applicationservices.StudentService;
import de.hhu.propra.uav.domains.applicationservices.UebungService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@SuppressWarnings({"PMD.AtLeastOneConstructor","PMD.BeanMembersShouldSerialize","PMD.AvoidDuplicateLiterals"})
@Controller
public class UebersichtController {

  @Autowired
  private UebungService uebungService;
  @Autowired
  private StudentService studentenService;

  @Secured("ROLE_ORGA")
  @GetMapping("verwaltung/uebersicht/uebungen")
  public String uebersicht(final Model model) {
    model.addAttribute("uebungen", uebungService.findAll());
    return "verwaltung/uebersichtVerwaltung";
  }

  @Secured("ROLE_ORGA")
  @GetMapping("verwaltung/uebersicht/{uebungId}/terminuebersicht")
  public String uebersichtTermine(final Model model, @PathVariable("uebungId") final Long uebungId) {
    model.addAttribute("uebung", uebungService.findById(uebungId));
    model.addAttribute("studenten", studentenService.findAllAsMap());
    return "verwaltung/uebersichtTermineVerwaltung";
  }

  @Secured("ROLE_ORGA")
  @GetMapping("verwaltung/uebersicht/studenten")
  public String uebersichtStudenten(final Model model) {
    model.addAttribute("studenten", studentenService.findAll());
    return "verwaltung/studentenUebersicht";
  }
}