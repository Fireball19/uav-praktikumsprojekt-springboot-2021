package de.hhu.propra.uav.repositories;

import de.hhu.propra.uav.domains.model.uebung.Uebung;
import de.hhu.propra.uav.domains.model.uebung.UebungRepository;
import org.springframework.data.repository.CrudRepository;

public interface JdbcUebungRepository extends CrudRepository<Uebung,Long>, UebungRepository {



}
