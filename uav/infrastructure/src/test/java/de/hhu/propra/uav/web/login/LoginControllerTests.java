package de.hhu.propra.uav.web.login;

import de.hhu.propra.uav.UebungsUndAnmeldungsverwaltungApplication;
import de.hhu.propra.uav.web.SetupOAuth2;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
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


import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(SpringExtension.class)
@SpringBootTest(
    webEnvironment = SpringBootTest.WebEnvironment.MOCK,
    classes = UebungsUndAnmeldungsverwaltungApplication.class)
@AutoConfigureMockMvc
public class LoginControllerTests {
  @Autowired
  private MockMvc mockMvc;

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
        .andExpect(redirectedUrl("/anmeldung"));
  }

  @Test
  public void testWithoutAuthentication() throws Exception {
    mockMvc.perform(MockMvcRequestBuilders.get("/"))
        .andExpect(status().isOk());
  }

}