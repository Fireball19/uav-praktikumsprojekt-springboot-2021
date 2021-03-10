package de.hhu.propra.uav.web.anmeldung;

import de.hhu.propra.uav.domains.services.AnmeldungService;
import de.hhu.propra.uav.domains.services.UebungService;
import de.hhu.propra.uav.domains.services.VerwaltungService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

@SuppressWarnings("PMD")
@Controller
public class AnmeldungController {

    @Autowired
    private UebungService uebungService;
    @Autowired
    private AnmeldungService anmeldungService;

    @Secured("ROLE_USER")
    @GetMapping("/anmeldung")
    public String anmeldung(final Model model) {
        model.addAttribute("uebungen", uebungService.findAll());
        return "anmeldung";
    }

    @Secured("ROLE_USER")
    @GetMapping("/anmeldung/{uebungId}")
    public String anmeldungTermin(final Model model, @PathVariable("uebungId") final Long uebungId) {
        model.addAttribute("uebung", uebungService.findById(uebungId));
        return "anmeldungTermin";
    }

    @Secured("ROLE_USER")
    @GetMapping("/anmeldung/{uebungId}/{terminId}")
    public String gruppenAnmeldung(final Model model, @PathVariable("uebungId") final Long uebungId,
                                  @PathVariable("terminId") final Long terminId) {
        return "gruppenAnmeldung";
    }

    @Secured("ROLE_USER")
    @PostMapping("/anmeldung/{uebungId}/{terminId}")
    public String gruppenAnmeldungAnmelden(final Model model, @PathVariable("uebungId") final Long uebungId,
                                  @PathVariable("terminId") final Long terminId,
                                           final String gruppenname, final String mitglieder) {
        anmeldungService.gruppenAnmeldung(uebungId, terminId, gruppenname, mitglieder);
        return "gruppenAnmeldung";
    }

}
