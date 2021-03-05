package de.hhu.propra.uav.domains.services;

import de.hhu.propra.uav.domains.uebung.Modus;
import de.hhu.propra.uav.domains.uebung.Uebung;
import de.hhu.propra.uav.repositories.UebungRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.springframework.http.HttpStatus.BAD_GATEWAY;
import static org.springframework.http.HttpStatus.NOT_FOUND;

@Service
public class UebungService {

  @Autowired
  UebungRepository uebungRepository;

  public Uebung createDefault() {
    return new Uebung("DEFAULT", Modus.DEFAULT, 0, 0, LocalDateTime.of(2000, 1, 1, 1, 1),
        LocalDateTime.of(2000, 1, 1, 1, 1));
  }

  public void save(Uebung uebung) {
    uebungRepository.save(uebung);
  }

  public Uebung findByName(String name) {
    return uebungRepository.findByName(name).orElseThrow(() ->
        new ResponseStatusException(NOT_FOUND, "Keine Übung mit " + name + " vorhanden."));


  }

  public List<Uebung> findAll() {
    return uebungRepository.findAll();
  }

}
