package de.hhu.propra.uav.web.verwaltung;

import de.hhu.propra.uav.UebungsUndAnmeldungsverwaltungApplication;
import de.hhu.propra.uav.domains.model.student.Student;
import de.hhu.propra.uav.domains.model.uebung.Modus;
import de.hhu.propra.uav.domains.model.uebung.Uebung;
import de.hhu.propra.uav.domains.services.StudentService;
import de.hhu.propra.uav.domains.services.UebungService;
import de.hhu.propra.uav.domains.services.VerwaltungService;
import de.hhu.propra.uav.web.SetupOAuth2;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.security.core.context.SecurityContextImpl;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.web.context.HttpSessionSecurityContextRepository;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.containsString;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
@SpringBootTest(
    webEnvironment = SpringBootTest.WebEnvironment.MOCK,
    classes = UebungsUndAnmeldungsverwaltungApplication.class)
@AutoConfigureMockMvc
public class VerwaltungControllerTests {
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
  public void uebungKonfigurationTest() throws Exception {
    OAuth2AuthenticationToken principal = SetupOAuth2.buildPrincipalOrga();
    MockHttpSession session = new MockHttpSession();
    session.setAttribute(
        HttpSessionSecurityContextRepository.SPRING_SECURITY_CONTEXT_KEY,
        new SecurityContextImpl(principal));

    when(uebungService.createDefault()).thenReturn(setUpUebungen().get(0));

    MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.get("/verwaltung/konfiguration/uebung")
        .session(session))
        .andExpect(status().isOk())
        .andExpect(content().string(containsString("Praktische Übung konfigurieren")))
        .andExpect(content().string(containsString("Praktische Übung erstellen")))
        .andReturn();

    Uebung uebung = (Uebung) mvcResult.getModelAndView().getModel().get("uebung");
    assertThat(uebung.getName()).isEqualTo("TestUebung1");
  }

  @Test
  public void uebungHinzufuegen() throws Exception {
    OAuth2AuthenticationToken principal = SetupOAuth2.buildPrincipalOrga();
    MockHttpSession session = new MockHttpSession();
    session.setAttribute(
        HttpSessionSecurityContextRepository.SPRING_SECURITY_CONTEXT_KEY,
        new SecurityContextImpl(principal));

    when(uebungService.createDefault()).thenReturn(setUpUebungen().get(0));

    mockMvc.perform(MockMvcRequestBuilders.post("/verwaltung/konfiguration/uebung")
        .session(session)
        .param("name","TestUebung")
        .param("modus","GRUPPENANMELDUNG")
        .param("maxGroesse","0")
        .param("minGroesse","1")
        .param("anmeldebeginn",LocalDateTime.now().plus(10,ChronoUnit.MINUTES).toString())
        .param("anmeldebeginn",LocalDateTime.now().plus(20,ChronoUnit.MINUTES).toString()))
        .andExpect(status().isForbidden())
        .andReturn();

    verify(uebungService,never()).save(any());
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
