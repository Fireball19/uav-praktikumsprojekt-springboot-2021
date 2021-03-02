package de.hhu.propra.uav.domains;

import lombok.Getter;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;


class Termin {
    private final LocalDateTime zeitpunkt;
    private boolean reserviert;
    private int minGroesse;
    private int maxGroesse;

    private Tutor tutor;
    @Getter
    private List<Student> studenten = new ArrayList<Student>();

    public Termin(String tutor, LocalDateTime zeitpunkt, int minGroesse, int maxGroesse) {
        this.tutor = new Tutor();
        this.zeitpunkt = zeitpunkt;
        this.minGroesse = minGroesse;
        this.maxGroesse = maxGroesse;
        this.reserviert = false;
    }
}
