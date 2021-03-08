package de.hhu.propra.uav.domains.services;

import de.hhu.propra.uav.domains.annotations.ApplicationService;
import de.hhu.propra.uav.domains.uebung.Modus;
import de.hhu.propra.uav.domains.uebung.Uebung;
import de.hhu.propra.uav.repositories.UebungRepository;
import org.springframework.http.HttpStatus;
import org.springframework.web.client.HttpClientErrorException;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;

@ApplicationService
public class UebungService {

  private final UebungRepository uebungRepository;

  public UebungService(UebungRepository uebungRepository){
    this.uebungRepository = uebungRepository;
  }

  public Uebung createDefault() {
    return new Uebung("DEFAULT", Modus.DEFAULT, 0, 0, LocalDateTime.now(),
        LocalDateTime.now().plus(1, ChronoUnit.WEEKS));
  }

  public void save(Uebung uebung) {
    uebungRepository.save(uebung);
  }

  public Uebung findById(Long id) {
    return uebungRepository.findById(id).orElseThrow(() ->
            new HttpClientErrorException(HttpStatus.NOT_FOUND,"Keine Uebung mit " + id + " vorhanden!"));
  }

  public Uebung findByName(String name) {
    return uebungRepository.findByName(name).orElseThrow(() ->
        new HttpClientErrorException(HttpStatus.NOT_FOUND,"Keine Uebung mit " + name + " vorhanden!"));
  }

  public List<Uebung> findAll() {
    return uebungRepository.findAll();
  }

}
