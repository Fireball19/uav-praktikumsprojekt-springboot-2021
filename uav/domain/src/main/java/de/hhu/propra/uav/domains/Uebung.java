package de.hhu.propra.uav.domains;

import lombok.Data;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Data
public class Uebung {
    private final String name;
    private final Modus modus;
    private final int minGroesse;
    private final int maxGroesse;
    private final LocalDateTime anmeldebeginn;
    private final LocalDateTime anmeldeschluss;

    private List<Termin> termine = new ArrayList<Termin>();

    public void terminHinzufuegen(String tutor, LocalDateTime zeitpunkt) {
        termine.add(new Termin(tutor, zeitpunkt, this.minGroesse, this.maxGroesse));
    }
}