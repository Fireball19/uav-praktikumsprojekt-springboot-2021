package de.hhu.propra.uav.web.login;

import de.hhu.propra.uav.domains.services.StudentService;
import de.hhu.propra.uav.domains.student.Student;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class LoginController {

  @Autowired
  StudentService studentenService;

  @GetMapping("/")
  public String index(@AuthenticationPrincipal OAuth2User principal, Model model) {
    if(principal != null) {
      String githubName = principal.getAttribute("login");
      studentenService.save(new Student(githubName));
      return "redirect:/anmeldung";
    }
    return "login";
  }
}
