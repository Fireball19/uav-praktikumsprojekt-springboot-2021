package de.hhu.propra.uav.web.verwaltung;

import de.hhu.propra.uav.domains.services.StudentService;
import de.hhu.propra.uav.domains.services.UebungService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
@SuppressWarnings("PMD")
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
    model.addAttribute("studenten", studentenService.findAllAsMap());
    return "uebersicht";
  }

  @Secured("ROLE_ORGA")
  @GetMapping("verwaltung/uebersicht/studenten")
  public String uebersichtStudenten(final Model model) {
    model.addAttribute("studenten", studentenService.findAll());
    return "studenten";
  }
}