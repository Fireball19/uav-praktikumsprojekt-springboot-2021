package de.hhu.propra.uav.authorization;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.List;

@SuppressWarnings("PMD")
@Component
public class AuthorityService {

  @Value("${arrayOfOrga}")
  private List<String> organisation;

  @Value("${arrayOfTutoren}")
  private List<String> tutoren;

  public boolean isOrga(final String username) {
    return organisation.contains(username);
  }

  public boolean isTutor(final String username) {
    return tutoren.contains(username);
  }
}
