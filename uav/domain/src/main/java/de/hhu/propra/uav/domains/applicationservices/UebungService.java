package de.hhu.propra.uav.domains.applicationservices;


import de.hhu.propra.uav.domains.annotations.ApplicationService;
import de.hhu.propra.uav.domains.github.GithubApi;
import de.hhu.propra.uav.domains.model.student.Student;
import de.hhu.propra.uav.domains.model.uebung.Modus;
import de.hhu.propra.uav.domains.model.uebung.TerminInfoDto;
import de.hhu.propra.uav.domains.model.uebung.Uebung;
import de.hhu.propra.uav.domains.model.uebung.UebungFactory;
import de.hhu.propra.uav.domains.model.uebung.UebungRepository;
import de.hhu.propra.uav.domains.model.uebung.VerteilungsService;
import de.hhu.propra.uav.domains.terminimporter.TerminFileDto;
import de.hhu.propra.uav.domains.terminimporter.TerminImporter;
import java.io.InputStream;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.http.HttpStatus;
import org.springframework.web.client.HttpClientErrorException;


@SuppressWarnings({"PMD.TooManyMethods", "PMD.AvoidDuplicateLiterals", "PMD.LongVariable"})
@ApplicationService
public class UebungService {

  @SuppressWarnings("PMD.BeanMembersShouldSerialize")
  private final UebungRepository uebungRepository;

  @SuppressWarnings("PMD.BeanMembersShouldSerialize")
  private final TerminImporter terminImporter;

  @SuppressWarnings("PMD.BeanMembersShouldSerialize")
  private final VerteilungsService verteilungsService;

  @SuppressWarnings("PMD.BeanMembersShouldSerialize")
  private final GithubApi githubApi;


  public UebungService(final UebungRepository uebungRepository, final TerminImporter terminImporter,
      final VerteilungsService verteilungsService, final GithubApi githubApi) {
    this.uebungRepository = uebungRepository;
    this.terminImporter = terminImporter;
    this.verteilungsService = verteilungsService;
    this.githubApi = githubApi;
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

  @SuppressWarnings("PMD.LawOfDemeter")
  public void abschliessen(final Long uebungId) {
    final Uebung uebung = findById(uebungId);
    uebung.abschliessen();
    save(uebung);
  }

  @SuppressWarnings("PMD.LawOfDemeter")
  public Uebung findById(final Long uebungId) {
    return uebungRepository.findById(uebungId).orElseThrow(() ->
        new HttpClientErrorException(HttpStatus.NOT_FOUND,
            "Keine Uebung mit " + uebungId + " vorhanden!"));
  }

  @SuppressWarnings("PMD.LawOfDemeter")
  public Uebung findByIdForStudent(final Long uebungId) {
    final Uebung uebung = findById(uebungId);
    final LocalDateTime jetzt = LocalDateTime.now();
    if (!jetzt.isBefore(uebung.getAnmeldeschluss())
        || !jetzt.isAfter(uebung.getAnmeldebeginn())) {
      throw new HttpClientErrorException(HttpStatus.FORBIDDEN, "Nicht im Anmeldezeitraum!");
    }
    return uebung;
  }

  @SuppressWarnings("PMD.LawOfDemeter")
  public Uebung findByName(final String name) {
    return uebungRepository.findByName(name).orElseThrow(() ->
        new HttpClientErrorException(HttpStatus.NOT_FOUND,
            "Keine Uebung mit " + name + " vorhanden!"));
  }

  public List<Uebung> findAll() {
    return uebungRepository.findAll();
  }

  @SuppressWarnings({"PMD.LawOfDemeter", "PMD.DataflowAnomalyAnalysis"})
  public List<Uebung> findAllForStudent() {
    final LocalDateTime jetzt = LocalDateTime.now();
    return uebungRepository.findAll().stream()
        .filter(x -> x.getAnmeldeschluss().isAfter(jetzt))
        .filter(x -> x.getAnmeldebeginn().isBefore(jetzt))
        .collect(Collectors.toList());
  }

  @SuppressWarnings("PMD.LawOfDemeter")
  public int ueberpruefeMaxGroesse(final Long uebungId) {
    final Uebung uebung = findById(uebungId);
    return uebung.getMaxGroesse();
  }

  @SuppressWarnings("PMD.LawOfDemeter")
  public int ueberpruefeMinGroesse(final Long uebungId) {
    final Uebung uebung = findById(uebungId);
    return uebung.getMinGroesse();
  }

  @SuppressWarnings("PMD.LawOfDemeter")
  public void addGruppe(final Long uebungId, final Long terminId, final String gruppenname) {
    final Uebung uebung = findById(uebungId);
    uebung.addGruppe(gruppenname, terminId);
    save(uebung);
  }

  @SuppressWarnings("PMD.LawOfDemeter")
  public void deleteGruppe(final Long uebungId, final Long terminId) {
    final Uebung uebung = findById(uebungId);
    uebung.deleteGruppe(terminId);
    save(uebung);
  }

  @SuppressWarnings("PMD.LawOfDemeter")
  public void addTermin(final long uebungId, final String tutor, final LocalDateTime zeitpunkt) {
    final Uebung uebung = findById(uebungId);
    uebung.addTermin(tutor, zeitpunkt);
    save(uebung);
  }

  @SuppressWarnings("PMD.LawOfDemeter")
  public Modus ueberpruefeAnmeldungsModus(final Long uebungId) {
    final Uebung uebung = findById(uebungId);
    return uebung.getModus();
  }

  public void addTermineByTerminImporter(final long uebungId, final InputStream inputStream) {
    final Uebung uebung = findById(uebungId);
    final List<TerminFileDto> termine = terminImporter.convertToTerminFile(inputStream);
    for (final TerminFileDto termin : termine) {
      uebung.addTermin(termin.getTutor(), termin.getZeitpunkt());
    }
    save(uebung);
  }

  public List<Uebung> findTermineByStudentId(final Student student) {
    final List<Uebung> uebungen = findAll();
    for (final Uebung uebung : uebungen) {
      final List<Long> termineIds = uebung.filterTerminIdsByStudent(student);
      for (final Long terminId : termineIds) {
        uebung.deleteTermin(terminId);
      }
    }

    return uebungen;
  }

  @SuppressWarnings("PMD.LawOfDemeter")
  public void deleteTermin(final Long uebungId, final Long terminId) {
    final Uebung uebung = findById(uebungId);
    uebung.deleteTermin(terminId);
    save(uebung);
  }

  public void shuffleTutoren(final Long uebungId) {
    final Uebung uebung = findById(uebungId);
    verteilungsService.tutorenVerteilen(uebung);
    save(uebung);
  }

  public List<Uebung> findAllIndividualAnmeldung() {
    return uebungRepository.findAllByModusEquals(Modus.INDIVIDUALANMELDUNG);
  }

  public void shuffleStudenten(final Long uebungId) {
    final Uebung uebung = findById(uebungId);
    verteilungsService.perfekteVerteilung(uebung);
    save(uebung);
  }

  @SuppressWarnings({"PMD.SignatureDeclareThrowsException", "PMD.LawOfDemeter"})
  public void individualModusAbschliessen(final Long uebungId) throws Exception {
    final Uebung uebung = findById(uebungId);
    final List<TerminInfoDto> terminInfoDtoList = uebung.getTerminDatenIndividualmodus();

    for (final TerminInfoDto terminInfoDto : terminInfoDtoList) {
      githubApi.createGithubRepositoryIndividualanmeldung(terminInfoDto.getStudenten(),
          uebung.getName(),
          terminInfoDto.getTutor(),
          terminInfoDto.getZeitpunkt());
    }

    uebung.abschliessen();
    save(uebung);
  }
}
