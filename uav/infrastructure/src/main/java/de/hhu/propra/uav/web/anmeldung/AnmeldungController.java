package de.hhu.propra.uav.web.anmeldung;

import de.hhu.propra.uav.domains.services.UebungService;
import de.hhu.propra.uav.repositories.JdbcStudentenRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class AnmeldungController {

    @Autowired
    UebungService uebungService;
    @Autowired
    JdbcStudentenRepository jdbcStudentenRepository;

    @GetMapping("/anmeldung")
    public String anmeldung(Model model) {
        model.addAttribute("uebungen", uebungService.findAll());
        return "anmeldung";
    }
}