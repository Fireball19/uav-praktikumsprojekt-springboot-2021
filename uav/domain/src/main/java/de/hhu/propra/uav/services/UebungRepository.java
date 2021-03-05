package de.hhu.propra.uav.services;

import de.hhu.propra.uav.domains.uebung.Uebung;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface UebungRepository extends CrudRepository<Uebung,Long> {

    public List<Uebung> findAll();

    public Uebung findByName(String name);

}
