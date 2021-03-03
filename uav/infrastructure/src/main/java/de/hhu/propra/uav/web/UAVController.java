package de.hhu.propra.uav.web;

import de.hhu.propra.uav.domains.Modus;
import de.hhu.propra.uav.domains.Uebung;
import de.hhu.propra.uav.persistence.UebungRepositoryImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import java.time.LocalDateTime;
import javax.validation.Valid;

@Controller
public class UAVController {

  @Autowired
  UebungRepositoryImpl uebungRepository;

  @GetMapping("/verwaltung")
  public String verwaltung(Model model, Uebung uebung, Errors errors) {
    model.addAttribute("uebung", new Uebung("DEFAULT", Modus.DEFAULT, 0, 0, LocalDateTime.of(2000, 1, 1, 1, 1),
        LocalDateTime.of(2000, 1, 1, 1, 1)));
    return "verwaltung";
  }

  @PostMapping("/verwaltung")
  public String uebungHinzufuegen(Model model, @Valid Uebung uebung, Errors errors) {
    
    if (errors.hasErrors()) {
      return "verwaltung";
    }

    model.addAttribute("uebung", uebung);
    uebungRepository.addUebung(uebung);

    return "redirect:/uebersicht";
  }

  @GetMapping("/uebersicht")
  public String uebersicht(Model model) {
    model.addAttribute("uebungen", uebungRepository.findAll());
    return "uebersicht";
  }

  @GetMapping("/")
  public String login() {
    return "login";
  }
}
