package de.hhu.propra.uav.web.login;

import de.hhu.propra.uav.domains.applicationservices.StudentService;
import de.hhu.propra.uav.web.SetupOAuth2;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.security.core.context.SecurityContextImpl;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.web.context.HttpSessionSecurityContextRepository;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;


import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(LoginController.class)
@AutoConfigureMockMvc
public class LoginControllerTests {
  @Autowired
  private MockMvc mockMvc;
  @MockBean
  private StudentService studentenService;

  @Test
  public void testWithAuthentication() throws Exception {
    OAuth2AuthenticationToken principal = SetupOAuth2.buildPrincipalUser();
    MockHttpSession session = new MockHttpSession();
    session.setAttribute(
        HttpSessionSecurityContextRepository.SPRING_SECURITY_CONTEXT_KEY,
        new SecurityContextImpl(principal));

    mockMvc.perform(MockMvcRequestBuilders.get("/")
        .session(session))
        .andExpect(status().isFound())
        .andExpect(redirectedUrl("/termine/uebersicht/uebungen"));
  }

  @Test
  public void testWithoutAuthentication() throws Exception {
    mockMvc.perform(MockMvcRequestBuilders.get("/"))
        .andExpect(status().isOk());
  }

}