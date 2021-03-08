package de.hhu.propra.uav.web.uebersicht;

import de.hhu.propra.uav.domains.services.UebungService;
import de.hhu.propra.uav.repositories.JdbcStudentenRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class UebersichtController {

    @Autowired
    UebungService uebungService;
    @Autowired
    JdbcStudentenRepository jdbcStudentenRepository;

    @GetMapping("verwaltung/uebersicht/uebungen")
    public String uebersicht(Model model) {
        model.addAttribute("uebungen", uebungService.findAll());
        return "uebersicht";
    }

    @GetMapping("verwaltung/uebersicht/studenten")
    public String uebersichtStudenten(Model model) {
        model.addAttribute("studenten", jdbcStudentenRepository.findAll());
        return "studenten";
    }
}