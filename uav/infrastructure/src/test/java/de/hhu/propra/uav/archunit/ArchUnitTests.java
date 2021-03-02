package de.hhu.propra.uav.archunit;

import com.tngtech.archunit.junit.AnalyzeClasses;
import com.tngtech.archunit.junit.ArchTest;
import com.tngtech.archunit.lang.ArchRule;
import de.hhu.propra.uav.UebungsUndAnmeldungsverwaltungApplication;

import static com.tngtech.archunit.library.Architectures.onionArchitecture;

@AnalyzeClasses(packagesOf = UebungsUndAnmeldungsverwaltungApplication.class)
public class ArchUnitTests {

  @ArchTest
  final static ArchRule onionArchitecture = onionArchitecture()
      .domainModels("de.hhu.propra.uav.domains..")
      .domainServices("de.hhu.propra.uav.services..")
      .applicationServices("de.hhu.propra.uav.domains..")
      .adapter("authorization","de.hhu.propra.uav.authorization")
      .adapter("persistence","de.hhu.propra.uav.persistence")
      .adapter("web","de.hhu.propra.uav.web");
}
