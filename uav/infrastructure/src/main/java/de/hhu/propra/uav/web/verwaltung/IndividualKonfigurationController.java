package de.hhu.propra.uav.web.verwaltung;

import de.hhu.propra.uav.domains.services.StudentService;
import de.hhu.propra.uav.domains.services.UebungService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class IndividualKonfigurationController {

  @Autowired
  private UebungService uebungService;
  @Autowired
  private StudentService studentService;

  @Secured("ROLE_ORGA")
  @GetMapping("/verwaltung/verteilung/individualmodus")
  public String individualUebersicht(Model model){
    model.addAttribute("uebungen",uebungService.findAllIndividualAnmeldung());
    return "individualUebersicht";
  }

  @Secured("ROLE_ORGA")
  @GetMapping("/verwaltung/verteilung/individualmodus/{uebungId}")
  public String individualKonfiguration(Model model, @PathVariable("uebungId") final Long uebungId){
    model.addAttribute("uebung",uebungService.findById(uebungId));
    model.addAttribute("studenten",studentService.findAllAsMap());
    return "individualKonfiguration";
  }

  @Secured("ROLE_ORGA")
  @PostMapping("/verwaltung/verteilung/individualmodus/{uebungId}/verteilen")
  public String individualVerteilen(@PathVariable("uebungId") final Long uebungId){
    uebungService.shuffleStudenten(uebungId);
    return "redirect:/verwaltung/verteilung/individualmodus/{uebungId}";
  }

}
