package de.hhu.propra.uav.web.anmeldung;

import de.hhu.propra.uav.authorization.AuthorityService;
import de.hhu.propra.uav.domains.services.UebungService;
import de.hhu.propra.uav.repositories.JdbcStudentenRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class AnmeldungController {

    @Autowired
    UebungService uebungService;
    @Autowired
    JdbcStudentenRepository jdbcStudentenRepository;
    @Autowired
    AuthorityService authorityService;

    @GetMapping("/anmeldung")
    public String anmeldung(@AuthenticationPrincipal OAuth2User principal, Model model) {
        model.addAttribute("isAuthorized", authorityService.isAuthorized(principal.getAttribute("login")));
        model.addAttribute("uebungen", uebungService.findAll());
        return "anmeldung";
    }
}
