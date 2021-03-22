package de.hhu.propra.uav.web.verwaltung.konfiguration;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import de.hhu.propra.uav.configuration.MethodSecurityConfiguration;
import de.hhu.propra.uav.domains.applicationservices.StudentService;
import de.hhu.propra.uav.domains.applicationservices.UebungService;
import de.hhu.propra.uav.domains.applicationservices.VerwaltungService;
import de.hhu.propra.uav.domains.model.student.Student;
import de.hhu.propra.uav.domains.model.uebung.Modus;
import de.hhu.propra.uav.domains.model.uebung.Uebung;
import de.hhu.propra.uav.web.SetupOAuth2;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.HashMap;
import java.util.List;
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

@WebMvcTest(TerminKonfigController.class)
@AutoConfigureMockMvc
@Import({MethodSecurityConfiguration.class})
@ActiveProfiles("test")
public class TerminKonfigControllerTest {
  @Autowired
  private MockMvc mockMvc;
  @MockBean
  private UebungService uebungService;
  @MockBean
  private StudentService studentService;
  @MockBean
  private VerwaltungService verwaltungService;

  public Uebung getUebung()
      throws ClassNotFoundException, NoSuchMethodException, InvocationTargetException, IllegalAccessException {
    Uebung uebung = new Uebung("TestUebung", Modus.INDIVIDUALANMELDUNG, 2, 3,
        LocalDateTime.now().minus(10, ChronoUnit.MINUTES),
        LocalDateTime.now().plus(10, ChronoUnit.MINUTES));
    uebung.setId(1L);
    uebung.addTermin("Alex",LocalDateTime.now());
    uebung.addTermin("Bob",LocalDateTime.now());

    Class clazz = Class.forName("de.hhu.propra.uav.domains.model.uebung.Termin");
    Method method = clazz.getDeclaredMethod("setId", Long.class);
    method.setAccessible(true);

    method.invoke(uebung.getTermine().get(0),1L);
    method.invoke(uebung.getTermine().get(1),2L);

    return uebung;
  }
  @Test
  public void terminKonfigurationTest() throws Exception {
    OAuth2AuthenticationToken principal = SetupOAuth2.buildPrincipalOrga();
    MockHttpSession session = new MockHttpSession();
    session.setAttribute(
        HttpSessionSecurityContextRepository.SPRING_SECURITY_CONTEXT_KEY,
        new SecurityContextImpl(principal));
    HashMap<Long, Student> studentMap = new HashMap<>();
    studentMap.put(1L,new Student("github301"));
    Uebung testUebung = getUebung();
    when(uebungService.findAll()).thenReturn(List.of(testUebung));

    MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.get("/verwaltung/konfiguration/termin")
        .principal(principal)
        .session(session))
        .andExpect(status().isOk())
        .andReturn();

    List<Uebung> uebung = (List<Uebung>) mvcResult.getModelAndView().getModel().get("uebungen");
    assertThat(uebung.get(0)).isEqualTo(testUebung);
  }

  @Test
  public void terminHinzufügenTest() throws Exception {
    OAuth2AuthenticationToken principal = SetupOAuth2.buildPrincipalOrga();
    MockHttpSession session = new MockHttpSession();
    session.setAttribute(
        HttpSessionSecurityContextRepository.SPRING_SECURITY_CONTEXT_KEY,
        new SecurityContextImpl(principal));
    Uebung testUebung = getUebung();
    when(uebungService.findById(any())).thenReturn(testUebung);

    MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.get("/verwaltung/konfiguration/termin/1")
        .principal(principal)
        .session(session))
        .andExpect(status().isOk())
        .andReturn();

    Uebung uebung = (Uebung) mvcResult.getModelAndView().getModel().get("uebung");
    assertThat(uebung).isEqualTo(testUebung);
  }

  @Test
  public void terminHinzufügenPostTest() throws Exception {
    OAuth2AuthenticationToken principal = SetupOAuth2.buildPrincipalOrga();
    MockHttpSession session = new MockHttpSession();
    session.setAttribute(
        HttpSessionSecurityContextRepository.SPRING_SECURITY_CONTEXT_KEY,
        new SecurityContextImpl(principal));
    Uebung testUebung = getUebung();
    when(uebungService.findById(any())).thenReturn(testUebung);

    MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.post("/verwaltung/konfiguration/termin/1/hinzufuegen")
        .principal(principal)
        .with(csrf())
        .param("tutor", "Alex")
        .param("zeitpunkt", LocalDateTime.of(2021,05,05,05,05).toString())
        .session(session))
        .andExpect(status().isFound())
        .andExpect(redirectedUrl("/verwaltung/konfiguration/termin/1"))
        .andReturn();

    verify(uebungService, times(1)).addTermin(1,"Alex",LocalDateTime.of(2021,05,05,05,05));
  }


  @Test
  public void terminEntfernenTest() throws Exception {
    OAuth2AuthenticationToken principal = SetupOAuth2.buildPrincipalOrga();
    MockHttpSession session = new MockHttpSession();
    session.setAttribute(
        HttpSessionSecurityContextRepository.SPRING_SECURITY_CONTEXT_KEY,
        new SecurityContextImpl(principal));
    Uebung testUebung = getUebung();
    when(uebungService.findById(any())).thenReturn(testUebung);

    MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.post("/verwaltung/konfiguration/termin/1/1/entfernen")
        .principal(principal)
        .with(csrf())
        .session(session))
        .andExpect(status().isFound())
        .andExpect(redirectedUrl("/verwaltung/konfiguration/termin/1"))
        .andReturn();

    verify(uebungService, times(1)).deleteTermin(1L,1L);
  }
}
