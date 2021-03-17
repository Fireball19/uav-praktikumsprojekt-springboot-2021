package de.hhu.propra.uav.web.anmeldung;

import de.hhu.propra.uav.domains.model.uebung.Modus;
import de.hhu.propra.uav.domains.applicationservices.UebungService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@SuppressWarnings("PMD")
@Controller
public class AnmeldungController {

  @Autowired
  private UebungService uebungService;

  @Secured("ROLE_USER")
  @GetMapping("/anmeldung")
  public String anmeldung(final Model model) {
    model.addAttribute("uebungen", uebungService.findAllForStudent());
    return "anmeldung/anmeldung";
  }

  @Secured("ROLE_USER")
  @GetMapping("/anmeldung/{uebungId}")
  public String anmeldungTermin(final Model model, @PathVariable("uebungId") final Long uebungId) {
    model.addAttribute("uebung", uebungService.findByIdForStudent(uebungId));
    if(uebungService.ueberpruefeAnmeldungsModus(uebungId) == Modus.INDIVIDUALANMELDUNG) {
      return "anmeldung/individualAnmeldung";
    }
    return "anmeldung/anmeldungTermin";
  }
}
