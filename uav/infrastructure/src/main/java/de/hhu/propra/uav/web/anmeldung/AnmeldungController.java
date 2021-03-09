package de.hhu.propra.uav.web.anmeldung;

import de.hhu.propra.uav.domains.services.UebungService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
@SuppressWarnings("PMD")
@Controller
public class AnmeldungController {

    @Autowired
    private UebungService uebungService;

    @Secured("ROLE_USER")
    @GetMapping("/anmeldung")
    public String anmeldung(final Model model) {
        model.addAttribute("uebungen", uebungService.findAll());
        return "anmeldung";
    }
}
