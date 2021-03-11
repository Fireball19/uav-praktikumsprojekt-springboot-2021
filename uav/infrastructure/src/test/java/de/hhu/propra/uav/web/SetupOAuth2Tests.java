package de.hhu.propra.uav.web;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.oauth2.core.user.OAuth2UserAuthority;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SetupOAuth2Tests {

  public static OAuth2AuthenticationToken buildPrincipalUser() {
    Map<String, Object> attributes = new HashMap<>();
    attributes.put("login","user");

    List<GrantedAuthority> authorities = Collections.singletonList(
        new OAuth2UserAuthority("ROLE_USER", attributes));
    OAuth2User user = new DefaultOAuth2User(authorities, attributes, "login");
    return new OAuth2AuthenticationToken(user, authorities, "whatever");
  }

  public static OAuth2AuthenticationToken buildPrincipalOrga() {
    Map<String, Object> attributes = new HashMap<>();
    attributes.put("login","orga");

    List<GrantedAuthority> authorities = Collections.singletonList(
        new OAuth2UserAuthority("ROLE_ORGA", attributes));
    OAuth2User user = new DefaultOAuth2User(authorities, attributes, "login");
    return new OAuth2AuthenticationToken(user, authorities, "whatever");
  }

  public static OAuth2AuthenticationToken buildPrincipalUserTutor() {
    Map<String, Object> attributes = new HashMap<>();
    attributes.put("login","tutor");

    List<GrantedAuthority> authorities = Collections.singletonList(
        new OAuth2UserAuthority("ROLE_TUTOR", attributes));
    OAuth2User user = new DefaultOAuth2User(authorities, attributes, "login");
    return new OAuth2AuthenticationToken(user, authorities, "whatever");
  }
}
