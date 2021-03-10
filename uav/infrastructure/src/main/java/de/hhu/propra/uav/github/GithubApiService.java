package de.hhu.propra.uav.github;

import org.kohsuke.github.*;
import org.springframework.beans.factory.annotation.Value;

import java.util.List;

@SuppressWarnings("PMD")
public class GithubApiService {
  @Value("${githubOrganization}")
  private String organization;

  private GitHub setup() throws Exception {

    final String appId = "103185";
    final long installationId = 15_103_623;

    final String jwt = JwtHelper.createJWT(".\\uav\\infrastructure\\key.der", appId, 60_000);

    final GitHub preAuth = new GitHubBuilder().withJwtToken(jwt).build();

    final GHAppInstallation appInstallation = preAuth.getApp().getInstallationById(installationId);
    GHAppInstallationToken token = appInstallation.createToken().create();

    return new GitHubBuilder().withAppInstallationToken(token.getToken()).build();

  }

  public void creqteGithubRepositoryGruppenAnmeldung(final String gruppenname, final String uebungsname,
                                                     List<String> mitglieder) throws Exception {
    GitHub gitHub = setup();
    GHOrganization ghOrganization = gitHub.getOrganization(this.organization);
    ghOrganization.createRepository(gruppenname+"-"+uebungsname).private_(true).create();
    GHRepository repository = ghOrganization.getRepository(gruppenname + "-" + uebungsname);

    for (String student : mitglieder) {
      repository.addCollaborators(gitHub.getUser(student));
    }
  }
}
