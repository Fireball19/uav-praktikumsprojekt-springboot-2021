package de.hhu.propra.uav.web.termin;

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
public class TutorController {

  @Autowired
  private UebungService uebungService;
  @Autowired
  private StudentService studentService;

  @Secured("ROLE_TUTOR")
  @GetMapping("tutor/uebersicht/{uebungId}/terminuebersicht")
  public String tutorUebersichtTermine(final Model model, @PathVariable("uebungId") final Long uebungId) {
    model.addAttribute("uebung", uebungService.findById(uebungId));
    model.addAttribute("studenten", studentService.findAllAsMap());
    return "termin/termineTutoren";
  }
}
