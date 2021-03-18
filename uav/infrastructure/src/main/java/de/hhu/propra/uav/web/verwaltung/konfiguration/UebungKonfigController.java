package de.hhu.propra.uav.web.verwaltung.konfiguration;

import de.hhu.propra.uav.domains.applicationservices.UebungService;
import de.hhu.propra.uav.domains.model.uebung.Uebung;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import javax.validation.Valid;

@SuppressWarnings({"PMD.AtLeastOneConstructor","PMD.BeanMembersShouldSerialize","PMD.AvoidDuplicateLiterals"})
@Controller
public class UebungKonfigController {

  @Autowired
  private UebungService uebungService;

  @Secured("ROLE_ORGA")
  @GetMapping("/verwaltung/konfiguration/uebung")
  public String uebungKonfiguration(final Model model) {
    model.addAttribute("uebung", uebungService.createDefault());
    return "verwaltung/uebungErstellen";
  }


  @SuppressWarnings({"PMD.LongVariable","PMD.OnlyOneReturn"})
  @Secured("ROLE_ORGA")
  @PostMapping("/verwaltung/konfiguration/uebung")
  public String uebungHinzufuegen(@Valid final Uebung uebung, final Errors errors, final boolean termineUebernehmen) {
    if (errors.hasErrors()) {
      return "verwaltung/uebungErstellen";
    }

    if (termineUebernehmen) {
      uebungService.saveWithAlteTermine(uebung);
    } else {
      uebungService.save(uebung);
    }

    return "redirect:/verwaltung/konfiguration/termin";
  }

}
