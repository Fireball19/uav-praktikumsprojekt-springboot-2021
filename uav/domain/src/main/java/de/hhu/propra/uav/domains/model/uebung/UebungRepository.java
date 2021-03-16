package de.hhu.propra.uav.domains.model.uebung;

import de.hhu.propra.uav.domains.model.uebung.Uebung;

import java.util.List;
import java.util.Optional;

@SuppressWarnings("PMD")
public interface UebungRepository {

   List<Uebung> findAll();

   Optional<Uebung> findByName(String name);

   Optional<Uebung> findById(Long id);

   Uebung save(Uebung uebung);

  Uebung findFirstByBearbeitetIsFalse();

  Uebung findTopByOrderByIdDesc();

  List<Uebung> findAllByModusEquals(Modus modus);
}
