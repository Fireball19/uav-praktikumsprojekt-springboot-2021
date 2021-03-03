package de.hhu.propra.uav.persistence;

import de.hhu.propra.uav.domains.Uebung;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;

@Repository
public class UebungRepositoryImpl {

  private List<Uebung> uebungen = new ArrayList<>();

  public List<Uebung> findAll() {
    return uebungen;
  }

  public void addUebung(Uebung uebung) {
    uebungen.add(uebung);
  }

}
