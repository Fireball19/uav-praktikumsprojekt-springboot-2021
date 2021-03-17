package de.hhu.propra.uav.web.login;

import de.hhu.propra.uav.domains.applicationservices.StudentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
@SuppressWarnings("PMD")
@Controller
public class LoginController {

  @Autowired
  private StudentService studentenService;

  @GetMapping("/")
  public String index(final @AuthenticationPrincipal OAuth2User principal, final Model model) {
    if(principal != null) {
      final String githubName = principal.getAttribute("login");
      studentenService.addStudent(githubName);
      return "redirect:/termine/uebersicht/uebungen";
    }
    return "login/login";
  }
}
