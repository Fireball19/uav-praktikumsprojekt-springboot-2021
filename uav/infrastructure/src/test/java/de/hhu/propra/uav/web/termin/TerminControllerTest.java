package de.hhu.propra.uav.web.termin;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import de.hhu.propra.uav.configuration.MethodSecurityConfiguration;
import de.hhu.propra.uav.domains.applicationservices.AnmeldungService;
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
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextImpl;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.oauth2.core.user.OAuth2UserAuthority;
import org.springframework.security.web.context.HttpSessionSecurityContextRepository;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

@WebMvcTest(TerminController.class)
@AutoConfigureMockMvc
@Import({MethodSecurityConfiguration.class})
@ActiveProfiles("test")
public class TerminControllerTest {
  @Autowired
  private MockMvc mockMvc;
  @MockBean
  private UebungService uebungService;
  @MockBean
  private AnmeldungService anmeldungService;
  @MockBean
  private StudentService studentService;
  @MockBean
  private VerwaltungService verwaltungService;

  public static OAuth2AuthenticationToken buildPrincipalTutor() {
    Map<String, Object> attributes = new HashMap<>();
    attributes.put("login", "tutor");

    List<GrantedAuthority> authorities = List.of(
        new OAuth2UserAuthority("ROLE_TUTOR", attributes),
        new OAuth2UserAuthority("ROLE_USER", attributes));
    OAuth2User user = new DefaultOAuth2User(authorities, attributes, "login");
    return new OAuth2AuthenticationToken(user, authorities, "whatever");
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

  public List<Uebung> setUpUebungen() {
    List<Uebung> uebungen = new ArrayList<>();
    Uebung uebung1 = new Uebung("TestUebung1", Modus.GRUPPENANMELDUNG, 2, 4,
        LocalDateTime.now().minus(10, ChronoUnit.MINUTES),
        LocalDateTime.now().plus(10, ChronoUnit.MINUTES));
    Uebung uebung2 = new Uebung("TestUebung2", Modus.INDIVIDUALANMELDUNG, 2, 4,
        LocalDateTime.now().minus(10, ChronoUnit.MINUTES),
        LocalDateTime.now().plus(10, ChronoUnit.MINUTES));
    uebungen.add(uebung1);
    uebungen.add(uebung2);
    return uebungen;
  }

  @Test
  public void tutoruebersichtStudentenTest() throws Exception {
    OAuth2AuthenticationToken principal = SetupOAuth2.buildPrincipalUser();
    MockHttpSession session = new MockHttpSession();
    session.setAttribute(
        HttpSessionSecurityContextRepository.SPRING_SECURITY_CONTEXT_KEY,
        new SecurityContextImpl(principal));
    HashMap<Long, Student> studentMap = new HashMap<>();
    studentMap.put(1L,new Student("github301"));
    when(studentService.findAllAsMap()).thenReturn(studentMap);

    MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.get("/termine/uebersicht/uebungen")
        .principal(principal)
        .session(session))
        .andExpect(status().isOk())
        .andReturn();

    verify(studentService, times(1)).findByGithub(any());
    verify(uebungService, times(1)).findTermineByStudentId(any());
    verify(studentService,times(1)).findAllAsMap();

    Map<Long,Student> studenten = (Map<Long, Student>) mvcResult.getModelAndView().getModel().get("studenten");
    assertThat(studenten.keySet()).contains(1L);
    assertThat(studenten.values()).contains(new Student("github301"));

  }

/* konnten dem Pricipal nicht die passenden Rechte geben
  @Test
  public void tutoruebersichtTest() throws Exception {
    OAuth2AuthenticationToken principal = buildPrincipalTutor();
    MockHttpSession session = new MockHttpSession();
    session.setAttribute(
        HttpSessionSecurityContextRepository.SPRING_SECURITY_CONTEXT_KEY,
        new SecurityContextImpl(principal));

    when(uebungService.findById(any())).thenReturn(setUpUebungen().get(0));
    MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.get("/termine/uebersicht/uebungen")
        .principal(principal)
        .session(session))
        .andExpect(status().isOk())
        .andReturn();


    verify(uebungService, times(1)).findAll();


  }
*/
  @Test
  public void abmeldenTest() throws Exception {
    OAuth2AuthenticationToken principal = SetupOAuth2.buildPrincipalUser();
    MockHttpSession session = new MockHttpSession();
    session.setAttribute(
        HttpSessionSecurityContextRepository.SPRING_SECURITY_CONTEXT_KEY,
        new SecurityContextImpl(principal));

    when(uebungService.findById(any())).thenReturn(setUpUebungen().get(0));
    MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.post("/termine/uebersicht/uebungen/1/1/abmelden")
        .with(csrf())
        .principal(principal)
        .session(session))
        .andExpect(status().isFound())
        .andExpect(redirectedUrl("/termine/uebersicht/uebungen"))
        .andReturn();

    verify(verwaltungService, times(1)).deleteStudent(any(),any(),any());

    }


}
