package de.hhu.propra.uav.web.verwaltung;

import de.hhu.propra.uav.domains.services.UebungService;
import de.hhu.propra.uav.domains.uebung.Uebung;
import de.hhu.propra.uav.repositories.StudentenRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import javax.validation.Valid;

@Controller
public class VerwaltungController {

    @Autowired
    UebungService uebungService;
    @Autowired
    StudentenRepository studentenRepository;

    @GetMapping("/verwaltung")
    public String verwaltung(Model model, Uebung uebung, Errors errors) {
        model.addAttribute("uebung", uebungService.createDefault());
        return "verwaltung";
    }

    @PostMapping("/verwaltung")
    public String uebungHinzufuegen(Model model, @Valid Uebung uebung, Errors errors) {

        if (errors.hasErrors()) {
            return "verwaltung";
        }

        model.addAttribute("uebung", uebung);
        uebungService.save(uebung);

        return "redirect:/uebersicht";
    }

}
