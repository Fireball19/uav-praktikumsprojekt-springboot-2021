package de.hhu.propra.uav.domains.applicationservices;


import de.hhu.propra.uav.domains.annotations.ApplicationService;
import de.hhu.propra.uav.domains.github.GithubApi;
import de.hhu.propra.uav.domains.model.student.Student;
import de.hhu.propra.uav.domains.model.uebung.*;
import de.hhu.propra.uav.domains.terminimporter.TerminFileDTO;
import de.hhu.propra.uav.domains.terminimporter.TerminImporter;
import org.springframework.http.HttpStatus;
import org.springframework.web.client.HttpClientErrorException;

import java.io.InputStream;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@ApplicationService
public class UebungService {


  private final UebungRepository uebungRepository;

  private final TerminImporter terminImporter;

  private final VerteilungsService verteilungsService;

  private final GithubApi githubAPI;


  public UebungService(final UebungRepository uebungRepository, final TerminImporter terminImporter,
                       final VerteilungsService verteilungsService, final GithubApi githubAPI){
    this.uebungRepository = uebungRepository;
    this.terminImporter = terminImporter;
    this.verteilungsService = verteilungsService;
    this.githubAPI = githubAPI;
  }


  public Uebung createDefault() {
    return UebungFactory.createDefault();
  }

  public void saveWithAlteTermine(final Uebung uebung) {
    final Uebung uebungToSave = UebungFactory.createWithAlteTermine(uebung.getName(),
        uebung.getModus(), uebung.getMinGroesse(),
        uebung.getMaxGroesse(), uebung.getAnmeldebeginn(), uebung.getAnmeldeschluss(),
        uebungRepository.findTopByOrderByIdDesc());
    save(uebungToSave);
  }

  public void save(final Uebung uebung) {
    uebungRepository.save(uebung);
  }

  public Uebung findFirstByBearbeitetIsFalse() {
    return uebungRepository.findFirstByBearbeitetIsFalse();
  }

  public void abschliessen(final Long uebungId) {
    final Uebung uebung = findById(uebungId);
    uebung.abschliessen();
    save(uebung);
  }

  public Uebung findById(final Long uebungId) {
    return uebungRepository.findById(uebungId).orElseThrow(() ->
            new HttpClientErrorException(HttpStatus.NOT_FOUND,"Keine Uebung mit " + uebungId + " vorhanden!"));
  }

  public Uebung findByIdForStudent(final Long uebungId) {
    final Uebung uebung = findById(uebungId);
    if(!LocalDateTime.now().isBefore(uebung.getAnmeldeschluss())
        || !LocalDateTime.now().isAfter(uebung.getAnmeldebeginn())) {
      throw new HttpClientErrorException(HttpStatus.FORBIDDEN,"Nicht im Anmeldezeitraum!");
    }
    return uebung;
  }


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

  public int ueberpruefeMaxGroesse(final Long uebungId) {
    return findById(uebungId).getMaxGroesse();
  }
  public int ueberpruefeMinGroesse(final Long uebungId) {
    return findById(uebungId).getMinGroesse();
  }

  public void addGruppe(final Long uebungId, final Long terminId, final String gruppenname) {
    final Uebung uebung = findById(uebungId);
    uebung.addGruppe(gruppenname, terminId);
    save(uebung);
  }

  public void deleteGruppe(final Long uebungId, final Long terminId) {
    final Uebung uebung = findById(uebungId);
    uebung.deleteGruppe(terminId);
    save(uebung);
  }

  public void addTermin(final long uebungId, final String tutor, final LocalDateTime zeitpunkt) {
    final Uebung uebung = findById(uebungId);
    uebung.addTermin(tutor, zeitpunkt);
    save(uebung);
  }

  public Modus ueberpruefeAnmeldungsModus(final Long uebungId) {
    return findById(uebungId).getModus();
  }

  public void addTermineByTerminImporter(final long uebungId, final InputStream inputStream) {
    final Uebung uebung = findById(uebungId);
    final List<TerminFileDTO> termine = terminImporter.convertToTerminFile(inputStream);
    for (final TerminFileDTO termin : termine) {
      uebung.addTermin(termin.getTutor(), termin.getZeitpunkt());
    }
    save(uebung);
  }

  public List<Uebung> findTermineByStudentId(final Student student) {
    final List<Uebung> uebungen = findAll();
    for (final Uebung uebung: uebungen) {
      final List<Long> termineIds = uebung.filterTerminIdsByStudent(student);
      for (final Long terminId : termineIds) {
        uebung.deleteTermin(terminId);
      }
    }

    return uebungen;
  }

  public void deleteTermin(final Long uebungId, final Long terminId){
    final Uebung uebung = findById(uebungId);
    uebung.deleteTermin(terminId);
    save(uebung);
  }

  public void shuffleTutoren(final Long uebungId) {
    final Uebung uebung = findById(uebungId);
    verteilungsService.tutorenVerteilen(uebung);
    save(uebung);
  }

  public List<Uebung> findAllIndividualAnmeldung(){
    return uebungRepository.findAllByModusEquals(Modus.INDIVIDUALANMELDUNG);
  }

  public void shuffleStudenten(final Long uebungId){
    final Uebung uebung = findById(uebungId);
    verteilungsService.perfekteVerteilung(uebung);
    save(uebung);
  }

  public void individualModusAbschliessen(final Long uebungId) throws Exception {
    final Uebung uebung = findById(uebungId);
    final List<TerminInfoDTO> terminInfoDTOList = uebung.getTerminDatenIndividualmodus();

    for (final TerminInfoDTO terminInfoDTO : terminInfoDTOList) {
      githubAPI.createGithubRepositoryIndividualanmeldung(terminInfoDTO.getStudenten(),
          uebung.getName(),
          terminInfoDTO.getTutor(),
          terminInfoDTO.getZeitpunkt());
    }

    uebung.abschliessen();
    save(uebung);
  }
}
