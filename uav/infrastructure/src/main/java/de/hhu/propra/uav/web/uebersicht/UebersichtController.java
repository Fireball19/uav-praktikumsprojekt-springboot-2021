package de.hhu.propra.uav.web.uebersicht;

import de.hhu.propra.uav.domains.services.UebungService;
import de.hhu.propra.uav.domains.student.Student;
import de.hhu.propra.uav.domains.uebung.Uebung;
import de.hhu.propra.uav.repositories.StudentenRepository;
import de.hhu.propra.uav.repositories.UebungRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import java.time.LocalDateTime;
import java.util.Optional;

@Controller
public class UebersichtController {

    @Autowired
    UebungService uebungService;
    @Autowired
    StudentenRepository studentenRepository;

    @GetMapping("/uebersicht/{name}")
    public String uebung(@PathVariable("name") String name, Model model) {
        model.addAttribute("uebung", uebungService.findByName(name));

        return "uebung";
    }

    @PostMapping("/uebersicht/{name}")
    public String termin(@PathVariable("name") String name, Model model, String tutor,
                         @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm") LocalDateTime zeitpunkt) {

        model.addAttribute("tutor", tutor);
        model.addAttribute("zeitpunkt", zeitpunkt);

        Uebung uebung = uebungService.findByName(name);
        uebung.addTermin(tutor, zeitpunkt);
        uebungService.save(uebung);

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
        Uebung uebung = uebungService.findByName(name);
        Optional<Student> student1 = studentenRepository.findByGithub(student);
        uebung.addStudent(student1.get(),zeitpunkt,tutor);
        uebungService.save(uebung);

        return "redirect:/uebersicht";
    }

    @PostMapping("/uebersicht/{name}/{tutor}/{zeitpunkt}/studentverschieben")
    public String studentVerschieben(@PathVariable("name") String name, @PathVariable("tutor") String tutor,
                                     @PathVariable("zeitpunkt")@DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm") LocalDateTime zeitpunkt,
                                     Model model, String student, String tutor2, @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm")LocalDateTime zeitpunkt2) {

        model.addAttribute("tutor", tutor2);
        model.addAttribute("zeitpunkt", zeitpunkt2);
        model.addAttribute(student);
        Uebung uebung = uebungService.findByName(name);
        Optional<Student> student1 = studentenRepository.findByGithub(student);
        uebung.moveStudent(student1.get(), zeitpunkt, tutor, zeitpunkt2, tutor2);
        uebungService.save(uebung);

        return "redirect:/uebersicht";
    }

    @GetMapping("/uebersicht")
    public String uebersicht(Model model) {
        model.addAttribute("uebungen", uebungService.findAll());
        return "uebersicht";
    }
}
