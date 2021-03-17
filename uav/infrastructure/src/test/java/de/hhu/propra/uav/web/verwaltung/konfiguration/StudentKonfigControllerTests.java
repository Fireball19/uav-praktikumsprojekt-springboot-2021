package de.hhu.propra.uav.web.verwaltung.konfiguration;

import de.hhu.propra.uav.configuration.MethodSecurityConfiguration;
import de.hhu.propra.uav.domains.applicationservices.StudentService;
import de.hhu.propra.uav.domains.applicationservices.UebungService;
import de.hhu.propra.uav.domains.applicationservices.VerwaltungService;
import de.hhu.propra.uav.domains.model.student.Student;
import de.hhu.propra.uav.domains.model.uebung.Modus;
import de.hhu.propra.uav.domains.model.uebung.Uebung;
import de.hhu.propra.uav.web.SetupOAuth2;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.security.core.context.SecurityContextImpl;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.web.context.HttpSessionSecurityContextRepository;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(StudentKonfigController.class)
@AutoConfigureMockMvc
@Import({MethodSecurityConfiguration.class})
@ActiveProfiles("test")
public class StudentKonfigControllerTests {
  @Autowired
  private MockMvc mockMvc;
  @MockBean
  private UebungService uebungService;
  @MockBean
  private StudentService studentService;
  @MockBean
  private VerwaltungService verwaltungService;

  public List<Uebung> setUpUebungen(){
    List<Uebung> uebungen = new ArrayList<>();
    Uebung uebung1 = new Uebung("TestUebung1", Modus.GRUPPENANMELDUNG, 2,4,
        LocalDateTime.now().minus(10, ChronoUnit.MINUTES),
        LocalDateTime.now().plus(10,ChronoUnit.MINUTES));
    Uebung uebung2 = new Uebung("TestUebung2", Modus.INDIVIDUALANMELDUNG, 2,4,
        LocalDateTime.now().minus(10, ChronoUnit.MINUTES),
        LocalDateTime.now().plus(10,ChronoUnit.MINUTES));
    uebungen.add(uebung1);
    uebungen.add(uebung2);
    return uebungen;
  }

  public List<Student> setUpStudenten(){
    List<Student> studenten = new ArrayList<>();
    Student student1 = new Student("Alex");
    Student student2 = new Student("Bob");
    studenten.add(student1);
    studenten.add(student2);
    return studenten;
  }

  @Test
  public void studentVerwaltungTest() throws Exception {
    OAuth2AuthenticationToken principal = SetupOAuth2.buildPrincipalOrga();
    MockHttpSession session = new MockHttpSession();
    session.setAttribute(
        HttpSessionSecurityContextRepository.SPRING_SECURITY_CONTEXT_KEY,
        new SecurityContextImpl(principal));

    when(studentService.findAll()).thenReturn(setUpStudenten());
    when(uebungService.findAll()).thenReturn(setUpUebungen());

    MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.get("/verwaltung/konfiguration/studenten")
        .session(session))
        .andExpect(status().isOk())
        .andReturn();

    List<Uebung> uebungen = (List<Uebung>) mvcResult.getModelAndView().getModel().get("uebungen");
    List<Student> studenten = (List<Student>) mvcResult.getModelAndView().getModel().get("studenten");
    assertThat(uebungen.get(0).getName()).isEqualTo("TestUebung1");
    assertThat(studenten.get(0).getGithub()).isEqualTo("Alex");
  }
}
