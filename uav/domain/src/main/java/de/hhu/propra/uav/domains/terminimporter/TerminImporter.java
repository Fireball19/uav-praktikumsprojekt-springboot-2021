package de.hhu.propra.uav.domains.terminimporter;

import java.io.InputStream;
import java.util.List;

public interface TerminImporter {

  public List<TerminFile> convertToTerminFile(InputStream inputStream);
}
