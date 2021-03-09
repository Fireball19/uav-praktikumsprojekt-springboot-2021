package de.hhu.propra.uav.web.verwaltung;

import de.hhu.propra.uav.authorization.AuthorityService;
import de.hhu.propra.uav.domains.services.StudentService;
import de.hhu.propra.uav.domains.services.UebungService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class UebersichtController {

  @Autowired
  UebungService uebungService;
  @Autowired
  StudentService studentenService;

  @Secured("ROLE_ORGA")
  @GetMapping("verwaltung/uebersicht/uebungen")
  public String uebersicht(Model model) {
    model.addAttribute("uebungen", uebungService.findAll());
    return "uebersicht";
  }

  @Secured("ROLE_ORGA")
  @GetMapping("verwaltung/uebersicht/studenten")
  public String uebersichtStudenten(Model model) {
    model.addAttribute("studenten", studentenService.findAll());
    return "studenten";
  }
}