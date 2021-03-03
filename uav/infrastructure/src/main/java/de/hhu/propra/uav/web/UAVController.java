package de.hhu.propra.uav.web;

import de.hhu.propra.uav.domains.Modus;
import de.hhu.propra.uav.domains.Uebung;
import de.hhu.propra.uav.persistence.UebungRepositoryImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import java.time.LocalDateTime;

@Controller
public class UAVController {

  @Autowired
  UebungRepositoryImpl uebungRepository;

  @GetMapping("/verwaltung")
  public String verwaltung(Model model, Uebung uebung) {
    model.addAttribute("uebung", new Uebung("DEFAULT", Modus.DEFAULT, 0, 0, LocalDateTime.of(1, 1, 1, 1, 1),
        LocalDateTime.of(1, 1, 1, 1, 1)));
    return "verwaltung";
  }

  @PostMapping("/verwaltung")
  public String uebungHinzufuegen(Model model, Uebung uebung, Modus modus) {

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
