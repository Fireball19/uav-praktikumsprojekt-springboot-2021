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

    @GetMapping("verwaltung/uebersicht/uebungen")
    public String uebersicht(Model model) {
        model.addAttribute("uebungen", uebungService.findAll());
        return "uebersicht";
    }

    @GetMapping("verwaltung/uebersicht/studenten")
    public String uebersichtStudenten(Model model) {
        model.addAttribute("studenten", studentenRepository.findAll());
        return "studenten";
    }
}