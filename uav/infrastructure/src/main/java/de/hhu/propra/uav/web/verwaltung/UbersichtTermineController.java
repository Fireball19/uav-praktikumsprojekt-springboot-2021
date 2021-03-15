package de.hhu.propra.uav.web.verwaltung;


import de.hhu.propra.uav.domains.services.StudentService;
import de.hhu.propra.uav.domains.services.UebungService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;



@Controller
public class UbersichtTermineController {

  @Autowired
  UebungService uebungService;
  @Autowired
  StudentService studentService;

  @Secured("ROLE_ORGA")
  @GetMapping("verwaltung/uebersicht/{uebungId}/terminuebersicht")
  public String uebersichtTermine(final Model model, @PathVariable("uebungId") final Long uebungId) {
    model.addAttribute("uebung", uebungService.findById(uebungId));
    model.addAttribute("studenten", studentService.findAllAsMap());
    return "uebersichtTermineVerwaltung";
  }
}
