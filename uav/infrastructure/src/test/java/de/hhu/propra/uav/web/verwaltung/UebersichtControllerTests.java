package de.hhu.propra.uav.web.verwaltung;

import de.hhu.propra.uav.UebungsUndAnmeldungsverwaltungApplication;
import de.hhu.propra.uav.domains.services.StudentService;
import de.hhu.propra.uav.domains.services.UebungService;
import de.hhu.propra.uav.web.SetupOAuth2;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.security.core.context.SecurityContextImpl;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.web.context.HttpSessionSecurityContextRepository;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
@SpringBootTest(
    webEnvironment = SpringBootTest.WebEnvironment.MOCK,
    classes = UebungsUndAnmeldungsverwaltungApplication.class)
@AutoConfigureMockMvc
public class UebersichtControllerTests {
  @Autowired
  private MockMvc mockMvc;
  @Mock
  private UebungService uebungService;
  @Mock
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


}
