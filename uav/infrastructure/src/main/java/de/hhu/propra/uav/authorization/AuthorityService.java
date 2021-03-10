package de.hhu.propra.uav.authorization;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;

import java.util.List;

@SuppressWarnings("PMD")
@Service
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
