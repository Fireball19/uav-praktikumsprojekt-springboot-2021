package de.hhu.propra.uav.web.verwaltung;

import de.hhu.propra.uav.terminimporter.TerminImporterImpl;
import de.hhu.propra.uav.domains.services.UebungService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

@Controller
public class CSVController {

  @Autowired
  TerminImporterImpl terminImporterCSV;

  @Autowired
  UebungService uebungService;

  @PostMapping("/verwaltung/konfiguration/termin/{uebungId}/hinzufuegen/csv-file")
  public String uploadCSVFile(@RequestParam("file") final MultipartFile file,
                              @PathVariable("uebungId") final Long uebungId) {

    if (file.isEmpty()) {
      // TODO: Zeige Fehlermeldung in HTML
    } else {
      uebungService.addTermineByTerminImporter(uebungId, TerminImporterImpl.convertMultipartFileToInputStream(file));
    }

    return "redirect:/";
  }
}
