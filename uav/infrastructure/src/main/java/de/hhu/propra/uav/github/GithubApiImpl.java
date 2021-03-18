package de.hhu.propra.uav.github;

import de.hhu.propra.uav.domains.model.student.Student;
import de.hhu.propra.uav.domains.model.student.StudentRef;
import de.hhu.propra.uav.domains.model.uebung.GruppeDTO;
import de.hhu.propra.uav.domains.model.uebung.Modus;
import de.hhu.propra.uav.domains.model.uebung.Uebung;
import de.hhu.propra.uav.domains.github.GithubApi;
import de.hhu.propra.uav.domains.applicationservices.StudentService;
import de.hhu.propra.uav.domains.applicationservices.UebungService;
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
public class GithubApiImpl implements GithubApi {
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
    String repositoryName = (uebungsname + "-" + gruppenname).replace(" ", "-");
    ghOrganization.createRepository(repositoryName).private_(true).create();
    GHRepository repository = ghOrganization.getRepository(repositoryName);

    for (StudentRef studentRef : mitglieder) {
      Student student = studentService.findById(studentRef.getId());
      repository.addCollaborators(gitHub.getUser(student.getGithub()));
    }
  }

  @Override
  public void createGithubRepositoryIndividualanmeldung(final List<StudentRef> studenten, final String uebungsname,
                                                        final String tutor, final LocalDateTime zeitpunkt) throws Exception {
    GitHub gitHub = setup();
    GHOrganization ghOrganization = gitHub.getOrganization(this.organization);
    String repositoryName = (uebungsname + "-" + tutor + "-" + zeitpunkt.toString())
        .replace(" ", "-")
        .replace(":", "-");
    ghOrganization.createRepository(repositoryName)
        .private_(true)
        .create();
    GHRepository repository = ghOrganization.getRepository(repositoryName);
    for (StudentRef studentRef : studenten) {
      Student student = studentService.findById(studentRef.getId());
      repository.addCollaborators(gitHub.getUser(student.getGithub()));
    }
  }

  @Scheduled(fixedDelayString = "${intervall}")
  public void checkAnmeldeschluss() throws Exception {
    Uebung uebung = uebungService.findFirstByBearbeitetIsFalse();
    if(uebung == null) {
      return;
    }
    if (LocalDateTime.now().isAfter(uebung.getAnmeldeschluss())
        && uebungService.ueberpruefeAnmeldungsModus(uebung.getId()) == Modus.GRUPPENANMELDUNG) {
      uebungService.shuffleTutoren(uebung.getId());
      List<GruppeDTO> gruppen = uebung.getGruppen();
      for (GruppeDTO gruppeDTO : gruppen) {
        createGithubRepositoryGruppenAnmeldung(gruppeDTO.getGruppenname(),
            uebung.getName(), gruppeDTO.getMitglieder());
      }
      uebungService.abschliessen(uebung.getId());
    }
  }
}
