package de.hhu.propra.uav.web.verwaltung;

import de.hhu.propra.uav.authorization.AuthorityService;
import de.hhu.propra.uav.domains.services.StudentService;
import de.hhu.propra.uav.domains.services.UebungService;
import de.hhu.propra.uav.domains.model.student.Student;
import de.hhu.propra.uav.domains.model.uebung.Uebung;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import javax.validation.Valid;
import java.time.LocalDateTime;

@Controller
public class VerwaltungController {

    @Autowired
    UebungService uebungService;
    @Autowired
    StudentService studentService;

    @Secured("ROLE_ORGA")
    @GetMapping("/verwaltung/konfiguration/uebung")
    public String uebungKonfiguration(Model model, Uebung uebung, Errors errors) {
        model.addAttribute("uebung", uebungService.createDefault());
        return "verwaltung";
    }


    @Secured("ROLE_ORGA")
    @PostMapping("/verwaltung/konfiguration/uebung")
    public String uebungHinzufuegen(Model model, @Valid Uebung uebung, Errors errors) {
        if (errors.hasErrors()) {
            return "verwaltung";
        }

        uebungService.save(uebung);

        return "redirect:/verwaltung/uebersicht/uebungen";
    }

    @Secured("ROLE_ORGA")
    @GetMapping("/verwaltung/konfiguration/termin")
    public String terminKonfiguration(Model model, Long id) {
        model.addAttribute("id", 0L);
        model.addAttribute("uebungen", uebungService.findAll());

        return "uebung";
    }

    @Secured("ROLE_ORGA")
    @PostMapping("/verwaltung/konfiguration/termin")
    public String terminHinzufuegen(Model model, Long id, String tutor,
                                    @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm") LocalDateTime zeitpunkt) {
        Uebung uebung = uebungService.findById(id);
        uebung.addTermin(tutor, zeitpunkt);
        uebungService.save(uebung);

        return "uebung";
    }

    @Secured("ROLE_ORGA")
    @GetMapping("/verwaltung/konfiguration/studenten")
    public String studentenVerwaltung(Model model) {
        model.addAttribute("studenten", studentService.findAll());
        model.addAttribute("uebungen", uebungService.findAll());

        return "termin";
    }

    @Secured("ROLE_ORGA")
    @PostMapping("/verwaltung/konfiguration/studenten")
    public String studentKonfigurieren(Model model, String github) {
        studentService.save(new Student(github));

        return "redirect:/verwaltung/konfiguration/studenten";
    }

    @Secured("ROLE_ORGA")
    @GetMapping("/verwaltung/konfiguration/studenten/{id}")
    public String studentenVerwaltung(Model model, @PathVariable("id") Long id) {
        model.addAttribute("uebung", uebungService.findById(id));

        return "terminKonfiguration";
    }

    @Secured("ROLE_ORGA")
    @PostMapping("/verwaltung/konfiguration/studenten/{id}/hinzufuegen")
    public String studentenTerminHinzufuegen(Model model, @PathVariable("id") Long id, String github, Long terminId) {
        Uebung uebung = uebungService.findById(id);
        Student student = studentService.findByGithub(github);

        System.out.println(terminId);
        uebung.addStudent(student, terminId);

        uebungService.save(uebung);
        return "redirect:/verwaltung/konfiguration/studenten/{id}";
    }

    @Secured("ROLE_ORGA")
    @PostMapping("/verwaltung/konfiguration/studenten/{id}/verschieben")
    public String studentenTerminVerschieben(Model model, @PathVariable("id") Long id,
                                             String github, Long terminAltId, Long terminNeuId) {
        Uebung uebung = uebungService.findById(id);
        Student student = studentService.findByGithub(github);

        uebung.moveStudent(student, terminAltId, terminNeuId);

        uebungService.save(uebung);
        return "redirect:/verwaltung/konfiguration/studenten/{id}";
    }

}
