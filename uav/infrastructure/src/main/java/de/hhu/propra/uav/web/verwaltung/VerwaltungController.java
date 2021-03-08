package de.hhu.propra.uav.web.verwaltung;

import de.hhu.propra.uav.domains.services.UebungService;
import de.hhu.propra.uav.domains.student.Student;
import de.hhu.propra.uav.domains.uebung.Uebung;
import de.hhu.propra.uav.repositories.JdbcStudentenRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import javax.validation.Valid;
import java.time.LocalDateTime;
import java.util.Optional;

@Controller
public class VerwaltungController {

    @Autowired
    UebungService uebungService;
    @Autowired
    JdbcStudentenRepository jdbcStudentenRepository;

    @GetMapping("/verwaltung/konfiguration/uebung")
    public String uebungKonfiguration(Model model, Uebung uebung, Errors errors) {
        model.addAttribute("uebung", uebungService.createDefault());
        return "verwaltung";
    }

    @PostMapping("/verwaltung/konfiguration/uebung")
    public String uebungHinzufuegen(Model model, @Valid Uebung uebung, Errors errors) {

        if (errors.hasErrors()) {
            return "verwaltung";
        }

        uebungService.save(uebung);

        return "redirect:/verwaltung/uebersicht/uebungen";
    }

    @GetMapping("/verwaltung/konfiguration/termin")
    public String terminKonfiguration(Model model, Long id) {
        model.addAttribute("id", Long.valueOf(0));
        model.addAttribute("uebungen", uebungService.findAll());

        return "uebung";
    }

    @PostMapping("/verwaltung/konfiguration/termin")
    public String terminHinzufuegen(Model model, Long id, String tutor,
                                    @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm") LocalDateTime zeitpunkt) {
        Uebung uebung = uebungService.findById(id);
        uebung.addTermin(tutor, zeitpunkt);
        uebungService.save(uebung);

        return "uebung";
    }

    @GetMapping("/verwaltung/konfiguration/studenten")
    public String studentenVerwaltung(Model model) {
        model.addAttribute("studenten", jdbcStudentenRepository.findAll());
        model.addAttribute("uebungen", uebungService.findAll());

        return "termin";
    }

    @PostMapping("/verwaltung/konfiguration/studenten")
    public String studentKonfigurieren(Model model, String github) {
        jdbcStudentenRepository.save(new Student(github));

        return "redirect:/verwaltung/konfiguration/studenten";
    }

    @GetMapping("/verwaltung/konfiguration/studenten/{id}")
    public String studentenVerwaltung(Model model, @PathVariable("id") Long id) {
        model.addAttribute("uebung", uebungService.findById(id));

        return "terminKonfiguration";
    }

    @PostMapping("/verwaltung/konfiguration/studenten/{id}/hinzufuegen")
    public String studentenTerminHinzufuegen(Model model, @PathVariable("id") Long id, String github, Long terminId) {
        Uebung uebung = uebungService.findById(id);
        Optional<Student> student = jdbcStudentenRepository.findByGithub(github);

        System.out.println(terminId);
        uebung.addStudent(student.get(), terminId);

        uebungService.save(uebung);
        return "redirect:/verwaltung/konfiguration/studenten/{id}";
    }

    @PostMapping("/verwaltung/konfiguration/studenten/{id}/verschieben")
    public String studentenTerminVerschieben(Model model, @PathVariable("id") Long id,
                                             String github, Long terminAltId, Long terminNeuId) {
        Uebung uebung = uebungService.findById(id);
        Optional<Student> student = jdbcStudentenRepository.findByGithub(github);

        uebung.moveStudent(student.get(), terminAltId, terminNeuId);

        uebungService.save(uebung);
        return "redirect:/verwaltung/konfiguration/studenten/{id}";
    }

}
