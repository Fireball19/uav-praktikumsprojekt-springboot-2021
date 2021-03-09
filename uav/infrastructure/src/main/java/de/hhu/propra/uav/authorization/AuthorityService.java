package de.hhu.propra.uav.authorization;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AuthorityService {

  @Value("${arrayOfOrga}")
  private List<String> organisation;

  public boolean checkAuthorization(String username) {
    return organisation.contains(username);
  }
}
