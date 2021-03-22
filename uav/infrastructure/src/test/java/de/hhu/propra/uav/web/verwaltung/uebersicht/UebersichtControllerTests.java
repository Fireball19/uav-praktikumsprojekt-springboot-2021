package de.hhu.propra.uav.web.verwaltung.uebersicht;

import de.hhu.propra.uav.ComponentScanConfiguration;
import de.hhu.propra.uav.configuration.MethodSecurityConfiguration;
import de.hhu.propra.uav.domains.applicationservices.StudentService;
import de.hhu.propra.uav.domains.applicationservices.UebungService;
import de.hhu.propra.uav.domains.model.uebung.Modus;
import de.hhu.propra.uav.domains.model.uebung.Uebung;
import de.hhu.propra.uav.web.SetupOAuth2;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
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

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(UebersichtController.class)
@AutoConfigureMockMvc
@Import({MethodSecurityConfiguration.class})
@ActiveProfiles("test")
public class UebersichtControllerTests {

  @Autowired
  private MockMvc mockMvc;
  @MockBean
  private UebungService uebungService;
  @MockBean
  private StudentService studentenService;

  @Test
  public void uebersichtAuthenticationOfUserTest() throws Exception {
    OAuth2AuthenticationToken principal = SetupOAuth2.buildPrincipalUser();
    MockHttpSession session = new MockHttpSession();
    session.setAttribute(
        HttpSessionSecurityContextRepository.SPRING_SECURITY_CONTEXT_KEY,
        new SecurityContextImpl(principal));

    mockMvc.perform(MockMvcRequestBuilders.get("/verwaltung/uebersicht/uebungen")
        .session(session))
        .andExpect(status().isForbidden());
  }

  @Test
  public void uebersichtStudentenAuthenticationOfUserTest() throws Exception {
    OAuth2AuthenticationToken principal = SetupOAuth2.buildPrincipalUser();
    MockHttpSession session = new MockHttpSession();
    session.setAttribute(
        HttpSessionSecurityContextRepository.SPRING_SECURITY_CONTEXT_KEY,
        new SecurityContextImpl(principal));

    mockMvc.perform(MockMvcRequestBuilders.get("/verwaltung/uebersicht/studenten")
        .session(session))
        .andExpect(status().isForbidden());
  }

  @Test
  public void uebersichtAuthenticationOfTutorTest() throws Exception {
    OAuth2AuthenticationToken principal = SetupOAuth2.buildPrincipalTutor();
    MockHttpSession session = new MockHttpSession();
    session.setAttribute(
        HttpSessionSecurityContextRepository.SPRING_SECURITY_CONTEXT_KEY,
        new SecurityContextImpl(principal));

    mockMvc.perform(MockMvcRequestBuilders.get("/verwaltung/uebersicht/uebungen")
        .session(session))
        .andExpect(status().isForbidden());
  }

  @Test
  public void uebersichtStudentenAuthenticationOfTutorTest() throws Exception {
    OAuth2AuthenticationToken principal = SetupOAuth2.buildPrincipalTutor();
    MockHttpSession session = new MockHttpSession();
    session.setAttribute(
        HttpSessionSecurityContextRepository.SPRING_SECURITY_CONTEXT_KEY,
        new SecurityContextImpl(principal));

    mockMvc.perform(MockMvcRequestBuilders.get("/verwaltung/uebersicht/studenten")
        .session(session))
        .andExpect(status().isForbidden());
  }

  @Test
  public void uebersichtAuthenticationOfOrgaTest() throws Exception {
    OAuth2AuthenticationToken principal = SetupOAuth2.buildPrincipalOrga();
    MockHttpSession session = new MockHttpSession();
    session.setAttribute(
        HttpSessionSecurityContextRepository.SPRING_SECURITY_CONTEXT_KEY,
        new SecurityContextImpl(principal));

    mockMvc.perform(MockMvcRequestBuilders.get("/verwaltung/uebersicht/uebungen")
        .session(session))
        .andExpect(status().isOk());
  }

  @Test
  public void uebersichtStudentenAuthenticationOfOrgaTest() throws Exception {
    OAuth2AuthenticationToken principal = SetupOAuth2.buildPrincipalOrga();
    MockHttpSession session = new MockHttpSession();
    session.setAttribute(
        HttpSessionSecurityContextRepository.SPRING_SECURITY_CONTEXT_KEY,
        new SecurityContextImpl(principal));

    mockMvc.perform(MockMvcRequestBuilders.get("/verwaltung/uebersicht/studenten")
        .session(session))
        .andExpect(status().isOk());
  }

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
  public void uebersichtTermineTest() throws Exception {
    OAuth2AuthenticationToken principal = SetupOAuth2.buildPrincipalOrga();
    MockHttpSession session = new MockHttpSession();
    session.setAttribute(
        HttpSessionSecurityContextRepository.SPRING_SECURITY_CONTEXT_KEY,
        new SecurityContextImpl(principal));
    Uebung testUebung = getUebung();
    when(uebungService.findById(any())).thenReturn(testUebung);

    MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.get("/verwaltung/uebersicht/1/terminuebersicht")
        .param("uebungId","1L")
        .principal(principal)
        .session(session))
        .andReturn();
    Uebung uebung = (Uebung) mvcResult.getModelAndView().getModel().get("uebung");

    assertThat(uebung).isEqualTo(testUebung);
    verify(uebungService,times(1)).findById(1L);
  }

}
