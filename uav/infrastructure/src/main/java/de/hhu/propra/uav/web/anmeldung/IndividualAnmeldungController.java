package de.hhu.propra.uav.web.anmeldung;

import de.hhu.propra.uav.domains.applicationservices.AnmeldungService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import java.time.LocalDateTime;

@Controller
public class IndividualAnmeldungController {

  @Autowired
  private AnmeldungService anmeldungService;

  @Secured("ROLE_USER")
  @PostMapping("/anmeldung/{uebungId}/{zeitpunkt}/individualanmeldung/anmelden")
  public String individualAnmeldung(@PathVariable("uebungId") final Long uebungId,
                                    @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm") @PathVariable("zeitpunkt")
                                    final LocalDateTime zeitpunkt,
                                    final @AuthenticationPrincipal OAuth2User principal) {
    anmeldungService.individualAnmeldung(uebungId,zeitpunkt, principal.getAttribute("login"));
    return "redirect:/anmeldung/{uebungId}";
  }
}
