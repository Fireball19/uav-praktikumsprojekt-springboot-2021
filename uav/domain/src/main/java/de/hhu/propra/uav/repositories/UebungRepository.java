package de.hhu.propra.uav.repositories;

import de.hhu.propra.uav.domains.uebung.Uebung;

import java.util.List;
import java.util.Optional;

public interface UebungRepository {

   List<Uebung> findAll();

   Optional<Uebung> findByName(String name);

   Optional<Uebung> findById(Long id);


   Uebung save(Uebung uebung);
}
