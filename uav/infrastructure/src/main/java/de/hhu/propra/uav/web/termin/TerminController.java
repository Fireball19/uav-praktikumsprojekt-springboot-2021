package de.hhu.propra.uav.web.termin;

import de.hhu.propra.uav.domains.applicationservices.StudentService;
import de.hhu.propra.uav.domains.applicationservices.UebungService;
import de.hhu.propra.uav.domains.model.student.Student;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@SuppressWarnings({"PMD.AtLeastOneConstructor", "PMD.BeanMembersShouldSerialize",
    "PMD.AvoidDuplicateLiterals"})
@Controller
public class TerminController {

  @Autowired
  private UebungService uebungService;

  @Autowired
  private StudentService studentService;

  @SuppressWarnings("PMD.OnlyOneReturn")
  @GetMapping("termine/uebersicht/uebungen")
  public String tutorUebersicht(final @AuthenticationPrincipal OAuth2User principal,
                                final Model model) {
    if (principal.getAuthorities().contains(new SimpleGrantedAuthority("ROLE_TUTOR"))) {
      model.addAttribute("uebungen", uebungService.findAll());
      return "termin/uebungenTutoren";
    }

    final String studentGithub = principal.getAttribute("login");
    final Student student = studentService.findByGithub(studentGithub);

    model.addAttribute("uebungen", uebungService.findTermineByStudentId(student));

    return "termin/termineStudenten";
  }
}
