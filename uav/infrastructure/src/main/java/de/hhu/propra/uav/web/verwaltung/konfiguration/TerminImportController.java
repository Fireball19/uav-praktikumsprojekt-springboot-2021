package de.hhu.propra.uav.web.verwaltung.konfiguration;

import de.hhu.propra.uav.domains.applicationservices.UebungService;
import de.hhu.propra.uav.terminimporter.TerminImporterImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;

@SuppressWarnings({"PMD.AtLeastOneConstructor", "PMD.BeanMembersShouldSerialize",
    "PMD.AvoidDuplicateLiterals"})
@Controller
public class TerminImportController {

  @Autowired
  private UebungService uebungService;

  @PostMapping("/verwaltung/konfiguration/termin/{uebungId}/hinzufuegen/csv-file")
  public String uploadCsvFile(@RequestParam("file") final MultipartFile file,
                              @PathVariable("uebungId") final Long uebungId) {

    if (file.isEmpty()) {
      throw new HttpClientErrorException(HttpStatus.EXPECTATION_FAILED, "Die Datei ist leer!");
    } else {
      uebungService.addTermineByTerminImporter(uebungId,
          convertMultipartFileToInputStream(file));
    }

    return "redirect:/verwaltung/konfiguration/termin/{uebungId}";
  }

  @SuppressWarnings({"PMD.AvoidFinalLocalVariable", "PMD.DataflowAnomalyAnalysis",
      "PMD.DataflowAnomalyAnalysis", "PMD.PreserveStackTrace"})
  private InputStream convertMultipartFileToInputStream(final MultipartFile file) {
    final InputStream inputStream;
    try {
      inputStream = file.getInputStream();
    } catch (IOException e) {
      throw new HttpClientErrorException(HttpStatus.INTERNAL_SERVER_ERROR,
          "Fehler beim einlesen der Datei!");
    }

    return inputStream;
  }
}
