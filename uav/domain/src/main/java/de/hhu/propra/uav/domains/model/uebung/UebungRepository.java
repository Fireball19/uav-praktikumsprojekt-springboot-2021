package de.hhu.propra.uav.domains.model.uebung;

import java.util.List;
import java.util.Optional;

public interface UebungRepository {

  List<Uebung> findAll();

  Optional<Uebung> findByName(String name);

  Optional<Uebung> findById(Long uebungId);

  Uebung save(Uebung uebung);

  Uebung findFirstByBearbeitetIsFalse();

  Uebung findTopByOrderByIdDesc();

  List<Uebung> findAllByModusEquals(Modus modus);
}
