package de.hhu.propra.uav.github;

import de.hhu.propra.uav.domains.model.student.Student;
import de.hhu.propra.uav.domains.model.student.StudentRef;
import de.hhu.propra.uav.domains.model.uebung.Gruppe;
import de.hhu.propra.uav.domains.model.uebung.Uebung;
import de.hhu.propra.uav.domains.services.GithubAPIService;
import de.hhu.propra.uav.domains.services.StudentService;
import de.hhu.propra.uav.domains.services.UebungService;
import org.kohsuke.github.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@SuppressWarnings("PMD")
@Service
@EnableScheduling
public class GithubApiServiceImpl implements GithubAPIService {
  @Value("${githubOrganization}")
  private String organization;
  
  @Autowired
  UebungService uebungService;
  @Autowired
  StudentService studentService;

  private GitHub setup() throws Exception {

    final String appId = "103185";
    final long installationId = 15_103_623;

    final String jwt = JwtHelper.createJWT(".\\uav\\infrastructure\\key.der", appId, 60_000);

    final GitHub preAuth = new GitHubBuilder().withJwtToken(jwt).build();

    final GHAppInstallation appInstallation = preAuth.getApp().getInstallationById(installationId);
    GHAppInstallationToken token = appInstallation.createToken().create();

    return new GitHubBuilder().withAppInstallationToken(token.getToken()).build();

  }

  public void createGithubRepositoryGruppenAnmeldung(final String gruppenname, final String uebungsname,
                                                     List<StudentRef> mitglieder) throws Exception {
    GitHub gitHub = setup();
    GHOrganization ghOrganization = gitHub.getOrganization(this.organization);
    ghOrganization.createRepository(gruppenname+"-"+uebungsname).private_(true).create();
    GHRepository repository = ghOrganization.getRepository(gruppenname + "-" + uebungsname);

    for (StudentRef studentRef : mitglieder) {
      Student student = studentService.findById(studentRef.getId());
      repository.addCollaborators(gitHub.getUser(student.getGithub()));
    }
  }

  @Scheduled(fixedDelayString = "${intervall}")
  public void checkAnmeldeschluss() throws Exception {
    System.out.println("Hallo!");
    Uebung uebung = uebungService.findFirstByBearbeitetIsFalse();
    if(uebung == null) {
      return;
    }
    if (LocalDateTime.now().isAfter(uebung.getAnmeldeschluss())) {
      System.out.println("Hallo2!");
      List<Gruppe> gruppen = uebung.getGruppen();
      for (Gruppe gruppe : gruppen) {
        createGithubRepositoryGruppenAnmeldung(gruppe.getGruppenname(),
            uebung.getName(), gruppe.getMitglieder());
      }
      uebungService.abschliessen(uebung.getId());
    }
  }
}
