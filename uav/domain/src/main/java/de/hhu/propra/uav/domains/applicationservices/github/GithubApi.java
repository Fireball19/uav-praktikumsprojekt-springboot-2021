package de.hhu.propra.uav.domains.applicationservices.github;

import de.hhu.propra.uav.domains.model.student.StudentRef;
import java.time.LocalDateTime;
import java.util.List;

@SuppressWarnings("PMD.SignatureDeclareThrowsException")
public interface GithubApi {


  void createGithubRepositoryGruppenAnmeldung(final String gruppenname, final String uebungsname,
      List<StudentRef> mitglieder) throws Exception;

  void createGithubRepositoryIndividualanmeldung(final List<StudentRef> studenten,
      final String uebungsname, final String tutor,
      final LocalDateTime zeitpunkt) throws Exception;
}
