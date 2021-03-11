package de.hhu.propra.uav.domains.services;

import de.hhu.propra.uav.domains.model.student.StudentRef;

import java.util.List;

public interface GithubAPIService {

  public void createGithubRepositoryGruppenAnmeldung(final String gruppenname, final String uebungsname,
                                                     List<StudentRef> mitglieder) throws Exception;
}
