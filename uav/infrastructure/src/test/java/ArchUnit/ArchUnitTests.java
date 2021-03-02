package ArchUnit;

import com.tngtech.archunit.junit.ArchTest;
import com.tngtech.archunit.lang.ArchRule;
import static com.tngtech.archunit.library.Architectures.onionArchitecture;

public class ArchUnitTests {

  @ArchTest
  final static ArchRule onionArchitecture = onionArchitecture()
      .domainModels("uav.domain..")
      .adapter("authorization","de.hhu.propra.authorization")
      .adapter("persistence","de.hhu.propra.persistence")
}
