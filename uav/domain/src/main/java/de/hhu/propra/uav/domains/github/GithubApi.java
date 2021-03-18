package de.hhu.propra.uav.domains.github;

import de.hhu.propra.uav.domains.model.student.StudentRef;

import java.time.LocalDateTime;
import java.util.List;

public interface GithubApi {

  void createGithubRepositoryGruppenAnmeldung(final String gruppenname, final String uebungsname,
                                                     List<StudentRef> mitglieder) throws Exception;

  void createGithubRepositoryIndividualanmeldung(final List<StudentRef> studenten,
                                                        final String uebungsname, final String tutor,
                                                        final LocalDateTime zeitpunkt) throws Exception;
}
