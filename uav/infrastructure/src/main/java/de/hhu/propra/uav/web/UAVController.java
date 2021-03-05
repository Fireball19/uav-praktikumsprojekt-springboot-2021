package de.hhu.propra.uav.web;

import de.hhu.propra.uav.domains.Modus;
import de.hhu.propra.uav.domains.Student;
import de.hhu.propra.uav.domains.Uebung;
import de.hhu.propra.uav.persistence.StudentenRepository;
import de.hhu.propra.uav.persistence.UebungRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import java.time.LocalDateTime;
import java.util.Optional;
import javax.validation.Valid;

@Controller
public class UAVController {

  @Autowired
  UebungRepository uebungRepository;
  @Autowired
  StudentenRepository studentenRepository;

  @GetMapping("/verwaltung")
  public String verwaltung(Model model, Uebung uebung, Errors errors) {
    model.addAttribute("uebung", new Uebung("DEFAULT", Modus.DEFAULT, 0, 0, LocalDateTime.of(2000, 1, 1, 1, 1),
        LocalDateTime.of(2000, 1, 1, 1, 1)));
    return "verwaltung";
  }
  
  @GetMapping("/uebersicht/{name}")
  public String uebung(@PathVariable("name") String name, Model model) {
    model.addAttribute("uebung", uebungRepository.findByName(name));
    
    return "uebung";
  }
  
  @PostMapping("/uebersicht/{name}")
  public String termin(@PathVariable("name") String name, Model model, String tutor, 
      @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm")LocalDateTime zeitpunkt) {
    
    model.addAttribute("tutor", tutor);
    model.addAttribute("zeitpunkt", zeitpunkt);

    Uebung uebung = uebungRepository.findByName(name);
    uebung.addTermin(tutor, zeitpunkt);
    uebungRepository.save(uebung);

    return "redirect:/uebersicht";
  }
  
  @GetMapping("/uebersicht/{name}/{tutor}/{zeitpunkt}")
  public String terminAnzeigen(@PathVariable("name") String name, @PathVariable("tutor") String tutor,
      @PathVariable("zeitpunkt")@DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm") LocalDateTime zeitpunkt, Model model) {

    return "termin";
  }
  
  @PostMapping("/uebersicht/{name}/{tutor}/{zeitpunkt}")
  public String student(@PathVariable("name") String name, @PathVariable("tutor") String tutor,
      @PathVariable("zeitpunkt")@DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm") LocalDateTime zeitpunkt, 
      Model model, String student) {
    
    model.addAttribute(student);
    Uebung uebung = uebungRepository.findByName(name);
    Optional<Student> student1 = studentenRepository.findByGithub(student);
    uebung.addStudent(student1.get(),zeitpunkt,tutor);
    uebungRepository.save(uebung);

    return "redirect:/uebersicht";
  }
  
  @PostMapping("/uebersicht/{name}/{tutor}/{zeitpunkt}/studentverschieben")
  public String studentVerschieben(@PathVariable("name") String name, @PathVariable("tutor") String tutor,
      @PathVariable("zeitpunkt")@DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm") LocalDateTime zeitpunkt, 
      Model model, String student, String tutor2, @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm")LocalDateTime zeitpunkt2) {
    
    model.addAttribute("tutor", tutor2);
    model.addAttribute("zeitpunkt", zeitpunkt2);
    model.addAttribute(student);
    Uebung uebung = uebungRepository.findByName(name);
    Optional<Student> student1 = studentenRepository.findByGithub(student);
    uebung.moveStudent(student1.get(), zeitpunkt, tutor, zeitpunkt2, tutor2);
    uebungRepository.save(uebung);

    return "redirect:/uebersicht";
  }

  @PostMapping("/verwaltung")
  public String uebungHinzufuegen(Model model, @Valid Uebung uebung, Errors errors) {
    
    if (errors.hasErrors()) {
      return "verwaltung";
    }

    model.addAttribute("uebung", uebung);
    uebungRepository.save(uebung);

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
