package de.hhu.propra.uav.terminimporter;

import com.opencsv.CSVReader;
import com.opencsv.CSVReaderBuilder;
import com.opencsv.exceptions.CsvValidationException;
import de.hhu.propra.uav.domains.terminimporter.TerminFile;
import de.hhu.propra.uav.domains.terminimporter.TerminImporter;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.List;

@Component
public class TerminImporterImpl implements TerminImporter {

  public List<TerminFile> convertToTerminFile(InputStream inputStream)  {
    List<TerminFile> termine = new ArrayList<>();

    try {
      Reader reader = new BufferedReader(new InputStreamReader(inputStream));
      CSVReader csvReader = new CSVReaderBuilder(reader).withSkipLines(1).build();

      String[] record;
      while ((record = csvReader.readNext()) != null) {
        termine.add(new TerminFile(record[0], LocalDateTime.parse(record[1],
            DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm"))));
      }

      csvReader.close();
      reader.close();

    } catch (CsvValidationException e) {
      throw new HttpClientErrorException(HttpStatus.INTERNAL_SERVER_ERROR,"Fehler in einer CSV Zeile!");
    } catch (IOException e) {
      throw new HttpClientErrorException(HttpStatus.INTERNAL_SERVER_ERROR,"Fehler beim einlesen der Datei!");
    } catch (DateTimeParseException e) {
      throw new HttpClientErrorException(HttpStatus.INTERNAL_SERVER_ERROR,"Ein Zeitpunkt ist nicht richtig formatiert");
    }

    return termine;
  }

  public static InputStream convertMultipartFileToInputStream(MultipartFile file) {
    InputStream inputStream = InputStream.nullInputStream();
    try {
      inputStream = file.getInputStream();
    } catch (IOException e) {
      new HttpClientErrorException(HttpStatus.INTERNAL_SERVER_ERROR,"Fehler beim einlesen der Datei!");
    }

    return inputStream;
  }
}
