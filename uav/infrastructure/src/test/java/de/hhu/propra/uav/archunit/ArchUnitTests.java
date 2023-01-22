package de.hhu.propra.uav.archunit;

import com.tngtech.archunit.junit.AnalyzeClasses;
import com.tngtech.archunit.junit.ArchTest;
import com.tngtech.archunit.lang.ArchRule;
import de.hhu.propra.uav.UebungsUndAnmeldungsverwaltungApplication;
import org.springframework.test.context.ActiveProfiles;

import static com.tngtech.archunit.library.Architectures.onionArchitecture;

@AnalyzeClasses(packagesOf = UebungsUndAnmeldungsverwaltungApplication.class)
@ActiveProfiles("test")
public class ArchUnitTests {

  @ArchTest
  final static ArchRule onionArchitecture = onionArchitecture()
      .domainModels("de.hhu.propra.uav.domains.model..")
      .domainServices("de.hhu.propra.uav.domains.model..")
      .applicationServices("de.hhu.propra.uav.domains.applicationservices..")
      .adapter("persistence", "de.hhu.propra.uav.repositories..")
      .adapter("web", "de.hhu.propra.uav.web..")
      .adapter("github", "de.hhu.propra.uav.github..")
      .adapter("terminimporter", "de.hhu.propra.uav.terminimporter..");
}
