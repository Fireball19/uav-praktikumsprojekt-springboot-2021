package de.hhu.propra.uav.domains;

import lombok.Getter;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class Termin {
    private String uebungName;
    @Getter
    private final LocalDateTime zeitpunkt;
    private boolean reserviert;
    private int minGroesse;
    private int maxGroesse;
    @Getter
    private final String tutor;
    @Getter
    private List<Student> studenten = new ArrayList<Student>();

    public Termin(String tutor, LocalDateTime zeitpunkt, int minGroesse, int maxGroesse) {
        this.tutor = tutor;
        this.zeitpunkt = zeitpunkt;
        this.minGroesse = minGroesse;
        this.maxGroesse = maxGroesse;
        this.reserviert = false;
    }

    public void addStudent(Student student){
        studenten.add(student);
    }

    public void deleteStudent(Student student) {
        studenten.remove(student);
    }

    @Override
    public String toString() {
        return tutor + " | " + zeitpunkt + " | " + reserviert + " | " + studenten;
    }
}
