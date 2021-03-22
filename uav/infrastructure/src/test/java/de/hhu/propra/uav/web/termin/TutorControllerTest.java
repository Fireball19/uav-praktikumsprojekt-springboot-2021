package de.hhu.propra.uav.web.termin;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import de.hhu.propra.uav.configuration.MethodSecurityConfiguration;
import de.hhu.propra.uav.domains.applicationservices.AnmeldungService;
import de.hhu.propra.uav.domains.applicationservices.StudentService;
import de.hhu.propra.uav.domains.applicationservices.UebungService;
import de.hhu.propra.uav.domains.applicationservices.VerwaltungService;
import de.hhu.propra.uav.domains.model.uebung.Modus;
import de.hhu.propra.uav.domains.model.uebung.Uebung;
import de.hhu.propra.uav.web.SetupOAuth2;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
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

@WebMvcTest(TutorController.class)
@AutoConfigureMockMvc
@Import({MethodSecurityConfiguration.class})
@ActiveProfiles("test")
public class TutorControllerTest {
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
  public void tutorUebersichtTermineTest() throws Exception {
    OAuth2AuthenticationToken principal = SetupOAuth2.buildPrincipalTutor();
    MockHttpSession session = new MockHttpSession();
    session.setAttribute(
        HttpSessionSecurityContextRepository.SPRING_SECURITY_CONTEXT_KEY,
        new SecurityContextImpl(principal));

    when(uebungService.findById(any())).thenReturn(setUpUebungen().get(0));
    MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.get("/tutor/uebersicht/1/terminuebersicht")
        .principal(principal)
        .session(session))
        .andExpect(status().isOk())
        .andReturn();

    verify(studentService, times(1)).findAllAsMap();
    verify(uebungService, times(1)).findById(any());
    verify(studentService,times(1)).findAllAsMap();

  }

}
