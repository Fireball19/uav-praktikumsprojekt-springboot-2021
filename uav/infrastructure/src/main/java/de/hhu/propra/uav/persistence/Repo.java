package de.hhu.propra.uav.persistence;

import de.hhu.propra.uav.domains.Uebung;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface Repo extends CrudRepository<Uebung,Long> {

    public List<Uebung> findAll();

    public Uebung findByName(String name);

}
