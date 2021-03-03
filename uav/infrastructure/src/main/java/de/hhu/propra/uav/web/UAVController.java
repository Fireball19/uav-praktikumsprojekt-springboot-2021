package de.hhu.propra.uav.web;

import de.hhu.propra.uav.domains.Modus;
import de.hhu.propra.uav.domains.Student;
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
    model.addAttribute("uebung", new Uebung("PÜ 0", Modus.GRUPPENANMELDUNG,
        1, 4, LocalDateTime.MIN, LocalDateTime.MIN));
    return "verwaltung";
  }

  @PostMapping("/verwaltung")
  public String uebungHinzufuegen(Model model, Uebung uebung) {

    model.addAttribute("uebung", uebung);
    /*
    Uebung uebung = new Uebung("PÜ 1", Modus.GRUPPENANMELDUNG,
        1, 4, LocalDateTime.MIN, LocalDateTime.MIN);
    uebung.terminHinzufuegen("Alex", LocalDateTime.of(2000,5,5,2,30));
    uebung.terminHinzufuegen("Justus", LocalDateTime.of(2012,10,5,3,35));
    uebung.addStudent(new Student("Student1"), LocalDateTime.of(2000,5,5,2,30), "Alex");

     */
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
