package de.hhu.propra.uav.web.verwaltung;

import de.hhu.propra.uav.authorization.AuthorityService;
import de.hhu.propra.uav.domains.services.StudentService;
import de.hhu.propra.uav.domains.services.UebungService;
import de.hhu.propra.uav.domains.student.Student;
import de.hhu.propra.uav.domains.uebung.Uebung;
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
    @Autowired
    AuthorityService authorityService;


    @GetMapping("/verwaltung/konfiguration/uebung")
    public String uebungKonfiguration(@AuthenticationPrincipal OAuth2User principal, Model model, Uebung uebung, Errors errors) {
        authorityService.checkAuthorization(principal.getAttribute("login"));
        model.addAttribute("isAuthorized", authorityService.isAuthorized(principal.getAttribute("login")));
        model.addAttribute("uebung", uebungService.createDefault());
        return "verwaltung";
    }


    @PostMapping("/verwaltung/konfiguration/uebung")
    public String uebungHinzufuegen(@AuthenticationPrincipal OAuth2User principal,Model model, @Valid Uebung uebung, Errors errors) {
        authorityService.checkAuthorization(principal.getAttribute("login"));
        model.addAttribute("isAuthorized", authorityService.isAuthorized(principal.getAttribute("login")));

        if (errors.hasErrors()) {
            return "verwaltung";
        }

        uebungService.save(uebung);

        return "redirect:/verwaltung/uebersicht/uebungen";
    }

    @GetMapping("/verwaltung/konfiguration/termin")
    public String terminKonfiguration(@AuthenticationPrincipal OAuth2User principal, Model model, Long id) {
        authorityService.checkAuthorization(principal.getAttribute("login"));
        model.addAttribute("isAuthorized", authorityService.isAuthorized(principal.getAttribute("login")));
        model.addAttribute("id", 0L);
        model.addAttribute("uebungen", uebungService.findAll());

        return "uebung";
    }


    @PostMapping("/verwaltung/konfiguration/termin")
    public String terminHinzufuegen(@AuthenticationPrincipal OAuth2User principal, Model model, Long id, String tutor,
                                    @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm") LocalDateTime zeitpunkt) {
        authorityService.checkAuthorization(principal.getAttribute("login"));
        model.addAttribute("isAuthorized", authorityService.isAuthorized(principal.getAttribute("login")));
        Uebung uebung = uebungService.findById(id);
        uebung.addTermin(tutor, zeitpunkt);
        uebungService.save(uebung);

        return "uebung";
    }


    @GetMapping("/verwaltung/konfiguration/studenten")
    public String studentenVerwaltung(@AuthenticationPrincipal OAuth2User principal, Model model) {
        authorityService.checkAuthorization(principal.getAttribute("login"));
        model.addAttribute("isAuthorized", authorityService.isAuthorized(principal.getAttribute("login")));
        model.addAttribute("studenten", studentService.findAll());
        model.addAttribute("uebungen", uebungService.findAll());

        return "termin";
    }


    @PostMapping("/verwaltung/konfiguration/studenten")
    public String studentKonfigurieren(@AuthenticationPrincipal OAuth2User principal, Model model, String github) {
        authorityService.checkAuthorization(principal.getAttribute("login"));
        model.addAttribute("isAuthorized", authorityService.isAuthorized(principal.getAttribute("login")));
        studentService.save(new Student(github));

        return "redirect:/verwaltung/konfiguration/studenten";
    }


    @GetMapping("/verwaltung/konfiguration/studenten/{id}")
    public String studentenVerwaltung(@AuthenticationPrincipal OAuth2User principal, Model model, @PathVariable("id") Long id) {
        authorityService.checkAuthorization(principal.getAttribute("login"));
        model.addAttribute("isAuthorized", authorityService.isAuthorized(principal.getAttribute("login")));
        model.addAttribute("uebung", uebungService.findById(id));

        return "terminKonfiguration";
    }


    @PostMapping("/verwaltung/konfiguration/studenten/{id}/hinzufuegen")
    public String studentenTerminHinzufuegen(@AuthenticationPrincipal OAuth2User principal, Model model, @PathVariable("id") Long id, String github, Long terminId) {
        authorityService.checkAuthorization(principal.getAttribute("login"));
        model.addAttribute("isAuthorized", authorityService.isAuthorized(principal.getAttribute("login")));
        Uebung uebung = uebungService.findById(id);
        Student student = studentService.findByGithub(github);

        System.out.println(terminId);
        uebung.addStudent(student, terminId);

        uebungService.save(uebung);
        return "redirect:/verwaltung/konfiguration/studenten/{id}";
    }

    @Secured("ROLE_TUTOR")
    @PostMapping("/verwaltung/konfiguration/studenten/{id}/verschieben")
    public String studentenTerminVerschieben(@AuthenticationPrincipal OAuth2User principal, Model model, @PathVariable("id") Long id,
                                             String github, Long terminAltId, Long terminNeuId) {
        authorityService.checkAuthorization(principal.getAttribute("login"));
        model.addAttribute("isAuthorized", authorityService.isAuthorized(principal.getAttribute("login")));
        Uebung uebung = uebungService.findById(id);
        Student student = studentService.findByGithub(github);

        uebung.moveStudent(student, terminAltId, terminNeuId);

        uebungService.save(uebung);
        return "redirect:/verwaltung/konfiguration/studenten/{id}";
    }

}
