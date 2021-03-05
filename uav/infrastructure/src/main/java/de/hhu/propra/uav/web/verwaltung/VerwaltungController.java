package de.hhu.propra.uav.web.verwaltung;

import de.hhu.propra.uav.domains.uebung.Modus;
import de.hhu.propra.uav.domains.uebung.Uebung;
import de.hhu.propra.uav.repositories.StudentenRepository;
import de.hhu.propra.uav.repositories.UebungRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import javax.validation.Valid;
import java.time.LocalDateTime;

@Controller
public class VerwaltungController {

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

    @PostMapping("/verwaltung")
    public String uebungHinzufuegen(Model model, @Valid Uebung uebung, Errors errors) {

        if (errors.hasErrors()) {
            return "verwaltung";
        }

        model.addAttribute("uebung", uebung);
        uebungRepository.save(uebung);

        return "redirect:/uebersicht";
    }

}
