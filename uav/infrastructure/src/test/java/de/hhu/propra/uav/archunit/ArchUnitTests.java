package de.hhu.propra.uav.archunit;

import com.tngtech.archunit.junit.AnalyzeClasses;
import com.tngtech.archunit.junit.ArchTest;
import com.tngtech.archunit.lang.ArchRule;
import de.hhu.propra.uav.UebungsUndAnmeldungsverwaltungApplication;
import de.hhu.propra.uav.authorization.AuthorityService;
import de.hhu.propra.uav.configuration.WebSecurityConfiguration;
import de.hhu.propra.uav.domains.annotations.ApplicationService;

import static com.tngtech.archunit.library.Architectures.onionArchitecture;

@AnalyzeClasses(packagesOf = UebungsUndAnmeldungsverwaltungApplication.class)
public class ArchUnitTests {

  @ArchTest
  final static ArchRule onionArchitecture = onionArchitecture()
      .domainModels("de.hhu.propra.uav.domains.model..")
      .domainServices("de.hhu.propra.uav.domains.services..")
      .applicationServices("de.hhu.propra.uav.domains..")
      .adapter("authorization", "de.hhu.propra.uav.authorization..")
      .adapter("persistence", "de.hhu.propra.uav.repositories..")
      .adapter("web", "de.hhu.propra.uav.web..")
      // Nice ArchUnit
      .ignoreDependency(ArchUnitTests.class, AuthorityService.class)
      .ignoreDependency(ArchUnitTests.class, ApplicationService.class)
      .ignoreDependency(WebSecurityConfiguration.class, AuthorityService.class)
      .ignoreDependency(UebungsUndAnmeldungsverwaltungApplication.class, ApplicationService.class);
}
