package de.hhu.propra.uav.web.verwaltung.verteilung;

import de.hhu.propra.uav.domains.applicationservices.StudentService;
import de.hhu.propra.uav.domains.applicationservices.UebungService;
import de.hhu.propra.uav.domains.applicationservices.VerwaltungService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

@SuppressWarnings({"PMD.AtLeastOneConstructor","PMD.BeanMembersShouldSerialize","PMD.AvoidDuplicateLiterals"})
@Controller
public class IndividualVerteilungController {

  @Autowired
  private UebungService uebungService;
  @Autowired
  private StudentService studentService;
  @Autowired
  private VerwaltungService verwaltungService;

  @Secured("ROLE_ORGA")
  @GetMapping("/verwaltung/verteilung/individualmodus")
  public String individualUebersicht(final Model model){
    model.addAttribute("uebungen",uebungService.findAllIndividualAnmeldung());
    return "verwaltung/individualUebersicht";
  }

  @Secured("ROLE_ORGA")
  @GetMapping("/verwaltung/verteilung/individualmodus/{uebungId}")
  public String individualKonfiguration(final Model model, @PathVariable("uebungId") final Long uebungId){
    model.addAttribute("uebung",uebungService.findById(uebungId));
    model.addAttribute("studenten",studentService.findAllAsMap());
    return "verwaltung/individualKonfiguration";
  }

  @Secured("ROLE_ORGA")
  @PostMapping("/verwaltung/verteilung/individualmodus/{uebungId}/verteilen")
  public String individualVerteilen(@PathVariable("uebungId") final Long uebungId){
    uebungService.shuffleStudenten(uebungId);
    return "redirect:/verwaltung/verteilung/individualmodus/{uebungId}";
  }

  @SuppressWarnings("PMD.SignatureDeclareThrowsException")
  @Secured("ROLE_ORGA")
  @PostMapping("/verwaltung/verteilung/individualmodus/{uebungId}/abschliessen")
  public String individualAbschliessen(@PathVariable("uebungId") final Long uebungId) throws Exception {
    uebungService.individualModusAbschliessen(uebungId);
    return "redirect:/verwaltung/verteilung/individualmodus/{uebungId}";
  }

  @Secured("ROLE_ORGA")
  @PostMapping("/verwaltung/verteilung/individualmodus/{uebungId}/verschieben")
  public String studentenVerschieben(@PathVariable("uebungId") final Long uebungId,
                                     final String github, final Long terminAltId, final Long terminNeuId) {
    verwaltungService.moveStudent(github,uebungId,terminAltId,terminNeuId);
    return "redirect:/verwaltung/verteilung/individualmodus/{uebungId}";
  }
}
