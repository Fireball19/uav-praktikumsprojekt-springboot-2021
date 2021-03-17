package de.hhu.propra.uav.configuration;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserService;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.authentication.HttpStatusEntryPoint;
import org.springframework.security.web.csrf.CookieCsrfTokenRepository;

import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

@SuppressWarnings("PMD")
@Configuration
@EnableWebSecurity
public class WebSecurityConfiguration extends WebSecurityConfigurerAdapter {

  @Value("${arrayOfOrga}")
  private List<String> organisation;

  @Value("${arrayOfTutoren}")
  private List<String> tutoren;

  @Override
  protected void configure(HttpSecurity http) throws Exception {
    http.authorizeRequests(a -> a
        .antMatchers("/", "/error", "/css/**", "/images/**").permitAll()
        .anyRequest().authenticated())
        .exceptionHandling(e -> e.authenticationEntryPoint(new HttpStatusEntryPoint(HttpStatus.UNAUTHORIZED)))
        .csrf(c -> c.csrfTokenRepository(CookieCsrfTokenRepository.withHttpOnlyFalse()))
        .logout(l -> l.logoutSuccessUrl("/").permitAll())
        .oauth2Login()
        .userInfoEndpoint()
        .userService(initOAuth2UserService());
  }

  private OAuth2UserService<OAuth2UserRequest, OAuth2User> initOAuth2UserService() {
    OAuth2UserService<OAuth2UserRequest, OAuth2User> oAuth2UserService = new DefaultOAuth2UserService();

    return oAuth2UserRequest -> {
      OAuth2User oAuth2User = oAuth2UserService.loadUser(oAuth2UserRequest);

      Map<String, Object> attributes = oAuth2User.getAttributes();
      String attributeNameKey = oAuth2UserRequest.getClientRegistration()
          .getProviderDetails().getUserInfoEndpoint().getUserNameAttributeName();

      Set<GrantedAuthority> authorities = new HashSet<>();

      // Standard USER Role hinzufügen
      authorities.add(new SimpleGrantedAuthority("ROLE_USER"));

      // Prüfen auf Rollen
      if (isOrga(attributes.get("login").toString())) {
        authorities.add(new SimpleGrantedAuthority("ROLE_ORGA"));
      }

      if (isTutor(attributes.get("login").toString())) {
        authorities.add(new SimpleGrantedAuthority("ROLE_TUTOR"));
      }

      return new DefaultOAuth2User(authorities, attributes, attributeNameKey);
    };
  }

  private boolean isOrga(final String username) {
    return organisation.contains(username);
  }

  private boolean isTutor(final String username) {
    return tutoren.contains(username);
  }

}