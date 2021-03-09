package de.hhu.propra.uav.authorization;

import org.kohsuke.github.*;

@SuppressWarnings("PMD")
public class AuthorizationHelper {

  public static void test() throws Exception {

    final String appId = "103185";
    final long installationId = 15_103_623;

    final String jwt = JwtHelper.createJWT(".\\uav\\infrastructure\\key.der", appId, 60_000);

    final GitHub preAuth = new GitHubBuilder().withJwtToken(jwt).build();

    final GHAppInstallation appInstallation = preAuth.getApp().getInstallationById(installationId);
    GHAppInstallationToken token = appInstallation.createToken().create();

    final GitHub gitHub = new GitHubBuilder().withAppInstallationToken(token.getToken()).build();

    GHOrganization organization = gitHub.getOrganization("hhu-propra-teamstrgclick-apitest");
    //GHRepository repository = organization.createRepository("foobar5").private_(true).create();
    //Map<String, GHRepository> repositories = organization.getRepositories();
    //GHRepository foobar = repositories.get("foobar");

    GHUser user = gitHub.getUser("ToxicBill");
    GHUser user1 = gitHub.getUser("Fireball19");
    GHUser user2 = gitHub.getUser("YangCAO-AKM");
    GHUser user3 = gitHub.getUser("zandatzu2");


    //foobar.addCollaborators(user3);
  }
}
