package de.hhu.propra.uav.web.verwaltung.konfiguration;

import de.hhu.propra.uav.terminimporter.TerminImporterImpl;
import de.hhu.propra.uav.domains.applicationservices.UebungService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.multipart.MultipartFile;

@SuppressWarnings({"PMD.AtLeastOneConstructor", "PMD.BeanMembersShouldSerialize", "PMD.AvoidDuplicateLiterals"})
@Controller
public class TerminImportController {

  @Autowired
  private UebungService uebungService;

  @PostMapping("/verwaltung/konfiguration/termin/{uebungId}/hinzufuegen/csv-file")
  public String uploadCSVFile(@RequestParam("file") final MultipartFile file,
                              @PathVariable("uebungId") final Long uebungId) {

    if (file.isEmpty()) {
      throw new HttpClientErrorException(HttpStatus.EXPECTATION_FAILED, "Die Datei ist leer!");
    } else {
      uebungService.addTermineByTerminImporter(uebungId, TerminImporterImpl.convertMultipartFileToInputStream(file));
    }

    return "redirect:/verwaltung/konfiguration/termin/{uebungId}";
  }
}
