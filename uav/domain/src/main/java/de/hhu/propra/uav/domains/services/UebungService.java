package de.hhu.propra.uav.domains.services;


import de.hhu.propra.uav.domains.annotations.ApplicationService;
import de.hhu.propra.uav.domains.model.uebung.Modus;
import de.hhu.propra.uav.domains.model.uebung.Uebung;
import de.hhu.propra.uav.domains.model.uebung.UebungRepository;
import de.hhu.propra.uav.domains.terminimporter.TerminFile;
import de.hhu.propra.uav.domains.terminimporter.TerminImporter;
import org.springframework.http.HttpStatus;
import org.springframework.web.client.HttpClientErrorException;

import java.io.InputStream;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.stream.Collectors;

@ApplicationService
public class UebungService {

  @SuppressWarnings("PMD")
  private final UebungRepository uebungRepository;

  private final TerminImporter terminImporter;

  @SuppressWarnings("PMD")
  public UebungService(final UebungRepository uebungRepository, final TerminImporter terminImporter){
    this.uebungRepository = uebungRepository;
    this.terminImporter = terminImporter;
  }

  @SuppressWarnings("PMD")
  public Uebung createDefault() {
    return new Uebung("DEFAULT", Modus.DEFAULT, 0, 0,
        LocalDateTime.now(),
        LocalDateTime.now().plus(1, ChronoUnit.WEEKS));
  }

  public void save(final Uebung uebung) {
    uebungRepository.save(uebung);
  }

  public Uebung findFirstByBearbeitetIsFalse() {
    return uebungRepository.findFirstByBearbeitetIsFalse();
  }

  public void abschliessen(final Long id) {
    Uebung uebung = findById(id);
    uebung.abschliessen();
    save(uebung);
  }

  @SuppressWarnings("PMD")
  public Uebung findById(final Long id) {
    return uebungRepository.findById(id).orElseThrow(() ->
            new HttpClientErrorException(HttpStatus.NOT_FOUND,"Keine Uebung mit " + id + " vorhanden!"));
  }

  public Uebung findByIdForStudent(final Long id) {
    Uebung uebung = findById(id);
    if(!LocalDateTime.now().isBefore(uebung.getAnmeldeschluss())
        || !LocalDateTime.now().isAfter(uebung.getAnmeldebeginn())) {
      throw new HttpClientErrorException(HttpStatus.FORBIDDEN,"Nicht im Anmeldezeitraum!");
    }
    return uebung;
  }

  @SuppressWarnings("PMD")
  public Uebung findByName(final String name) {
    return uebungRepository.findByName(name).orElseThrow(() ->
        new HttpClientErrorException(HttpStatus.NOT_FOUND,"Keine Uebung mit " + name + " vorhanden!"));
  }

  public List<Uebung> findAll() {
    return uebungRepository.findAll();
  }

  public List<Uebung> findAllForStudent() {
    return uebungRepository.findAll().stream()
        .filter(x -> x.getAnmeldeschluss().isAfter(LocalDateTime.now()))
        .filter(x -> x.getAnmeldebeginn().isBefore(LocalDateTime.now()))
        .collect(Collectors.toList());
  }

  public int ueberpruefeMaxGroesse(final Long Id) {
    return findById(Id).getMaxGroesse();
  }
  public int ueberpruefeMinGroesse(final Long Id) {
    return findById(Id).getMinGroesse();
  }

  public void addGruppe(final Long uebungId, final Long terminId, final String gruppenname) {
    Uebung uebung = findById(uebungId);
    uebung.addGruppe(gruppenname, terminId);
    save(uebung);
  }

  public void deleteGruppe(final Long uebungId, final Long terminId) {
    Uebung uebung = findById(uebungId);
    uebung.deleteGruppe(terminId);
    save(uebung);
  }

  public void addTermin(final long uebungId, final String tutor, final LocalDateTime zeitpunkt) {
    Uebung uebung = findById(uebungId);
    uebung.addTermin(tutor, zeitpunkt);
    save(uebung);
  }

  public Modus ueberpruefeAnmeldungsModus(final Long Id) {
    return findById(Id).getModus();
  }

  public void addTermineByTerminImporter(final long uebungId, final InputStream inputStream) {
    Uebung uebung = findById(uebungId);
    List<TerminFile> termine = terminImporter.convertToTerminFile(inputStream);
    for (TerminFile termin : termine) {
      uebung.addTermin(termin.getTutor(), termin.getZeitpunkt());
    }
    save(uebung);
  }
}
