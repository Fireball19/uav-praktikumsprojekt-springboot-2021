package de.hhu.propra.uav.terminimporter;

import com.opencsv.CSVReader;
import com.opencsv.CSVReaderBuilder;
import com.opencsv.exceptions.CsvValidationException;
import de.hhu.propra.uav.domains.terminimporter.TerminFileDto;
import de.hhu.propra.uav.domains.terminimporter.TerminImporter;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.multipart.MultipartFile;

@SuppressWarnings("PMD.AtLeastOneConstructor")
@Component
public class TerminImporterImpl implements TerminImporter {

  @SuppressWarnings({"PMD.DataflowAnomalyAnalysis", "PMD.CloseResource",
      "PMD.AssignmentInOperand", "PMD.PreserveStackTrace"})
  @Override
  public List<TerminFileDto> convertToTerminFile(final InputStream inputStream) {
    final List<TerminFileDto> termine = new ArrayList<>();

    try {
      final Reader reader =
          new BufferedReader(new InputStreamReader(inputStream, StandardCharsets.UTF_8));
      final CSVReader csvReader = new CSVReaderBuilder(reader).withSkipLines(1).build();

      String[] record;
      while ((record = csvReader.readNext()) != null) {
        termine.add(new TerminFileDto(record[0], LocalDateTime.parse(record[1],
            DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm"))));
      }

      csvReader.close();
      reader.close();

    } catch (CsvValidationException e) {
      throw new HttpClientErrorException(HttpStatus.INTERNAL_SERVER_ERROR,
          "Fehler in einer CSV Zeile!");
    } catch (IOException e) {
      throw new HttpClientErrorException(HttpStatus.INTERNAL_SERVER_ERROR,
          "Fehler beim einlesen der Datei!");
    } catch (DateTimeParseException e) {
      throw new HttpClientErrorException(HttpStatus.INTERNAL_SERVER_ERROR,
          "Ein Zeitpunkt ist nicht richtig formatiert");
    }
    return termine;
  }

  @SuppressWarnings({"PMD.AvoidFinalLocalVariable", "PMD.DataflowAnomalyAnalysis",
      "PMD.DataflowAnomalyAnalysis", "PMD.PreserveStackTrace"})
  public static InputStream convertMultipartFileToInputStream(final MultipartFile file) {
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
