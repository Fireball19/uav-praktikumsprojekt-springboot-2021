package de.hhu.propra.uav.web.verwaltung;


import de.hhu.propra.uav.domains.model.uebung.Uebung;
import de.hhu.propra.uav.domains.services.StudentService;
import de.hhu.propra.uav.domains.services.UebungService;
import de.hhu.propra.uav.domains.services.VerwaltungService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import javax.validation.Valid;
import java.time.LocalDateTime;
@SuppressWarnings("PMD")
@Controller
public class VerwaltungController {

    public static final String ROLE_ORGA = "ROLE_ORGA";
    @Autowired
    private UebungService uebungService;
    @Autowired
    private StudentService studentService;
    @Autowired
    private VerwaltungService verwaltungService;

    @Secured("ROLE_ORGA")
    @GetMapping("/verwaltung/konfiguration/uebung")
    public String uebungKonfiguration(final Model model) {
        model.addAttribute("uebung", uebungService.createDefault());
        return "verwaltung";
    }


    @Secured(ROLE_ORGA)
    @PostMapping("/verwaltung/konfiguration/uebung")
    public String uebungHinzufuegen(@Valid final Uebung uebung, final Errors errors) {
        if (errors.hasErrors()) {
            return "verwaltung";
        }

        uebungService.save(uebung);

        return "redirect:/verwaltung/uebersicht/uebungen";
    }

    @Secured(ROLE_ORGA)
    @GetMapping("/verwaltung/konfiguration/termin")
    public String terminKonfiguration(final Model model) {
        model.addAttribute("id", 0L);
        model.addAttribute("uebungen", uebungService.findAll());
        return "uebung";
    }

    @Secured(ROLE_ORGA)
    @PostMapping("/verwaltung/konfiguration/termin")
    public String terminHinzufuegen(final Long id, final String tutor,
                                    @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm") final LocalDateTime zeitpunkt) {
        uebungService.addTermin(id, tutor, zeitpunkt);
        return "uebung";
    }

    @Secured(ROLE_ORGA)
    @GetMapping("/verwaltung/konfiguration/studenten")
    public String studentenVerwaltung(final Model model) {
        model.addAttribute("studenten", studentService.findAll());
        model.addAttribute("uebungen", uebungService.findAll());

        return "termin";
    }

    @Secured(ROLE_ORGA)
    @PostMapping("/verwaltung/konfiguration/studenten")
    public String studentKonfigurieren(final String github) {
        studentService.addStudent(github);

        return "redirect:/verwaltung/konfiguration/studenten";
    }

    @Secured(ROLE_ORGA)
    @GetMapping("/verwaltung/konfiguration/studenten/{id}")
    public String studentenVerwaltung(final Model model, @PathVariable("id") final Long id) {
        model.addAttribute("uebung", uebungService.findById(id));
      model.addAttribute("studenten",studentService.findAllAsMap());

        return "terminKonfiguration";
    }

    @Secured(ROLE_ORGA)
    @PostMapping("/verwaltung/konfiguration/studenten/{id}/hinzufuegen")
    public String studentenTerminHinzufuegen(@PathVariable("id") final Long id, final String github, final Long terminId) {
        verwaltungService.addStudent(github, id, terminId);
        return "redirect:/verwaltung/konfiguration/studenten/{id}";
    }

    @Secured(ROLE_ORGA)
    @PostMapping("/verwaltung/konfiguration/studenten/{id}/entfernen")
    public String studentenTerminEntfernen(@PathVariable("id") final Long id, final String github, final Long terminId) {
        verwaltungService.deleteStudent(github, id, terminId);
        return "redirect:/verwaltung/konfiguration/studenten/{id}";
    }

    @Secured(ROLE_ORGA)
    @PostMapping("/verwaltung/konfiguration/studenten/{id}/verschieben")
    public String studentenTerminVerschieben(@PathVariable("id") final Long id,
                                             final String github, final Long terminAltId, final Long terminNeuId) {
        verwaltungService.moveStudent(github, id, terminAltId, terminNeuId);
        return "redirect:/verwaltung/konfiguration/studenten/{id}";
    }

}
