package de.hhu.propra.uav.domains;

import lombok.Data;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Data
public class Uebung {
    private final String name;
    private final Modus modus;
    private final int minGroesse;
    private final int maxGroesse;
    private final LocalDateTime anmeldebeginn;
    private final LocalDateTime anmeldeschluss;
    @Getter
    private List<Termin> termine = new ArrayList<>();

    public void terminHinzufuegen(String tutor, LocalDateTime zeitpunkt) {
        termine.add(new Termin(tutor, zeitpunkt, this.minGroesse, this.maxGroesse));
    }

    public void addStudent(Student student, LocalDateTime zeitpunkt, String tutor) {
        List<Termin> termin = termine.stream()
            .filter(x -> x.getZeitpunkt().equals(zeitpunkt) && x.getTutor().equals(tutor))
            .collect(Collectors.toList());

        if (!termin.isEmpty()) termin.get(0).addStudent(student);
    }

    public void deleteStudent(Student student, LocalDateTime zeitpunkt, String tutor) {
        List<Termin> termin = termine.stream()
            .filter(x -> x.getZeitpunkt().equals(zeitpunkt) && x.getTutor().equals(tutor))
            .collect(Collectors.toList());

        if (!termin.isEmpty()) termin.get(0).deleteStudent(student);
    }

    public void moveStudent(Student student, LocalDateTime zeitpunkt, String tutor, LocalDateTime zeitpunkt2, String tutor2) {
        addStudent(student, zeitpunkt2, tutor2);
        deleteStudent(student, zeitpunkt, tutor);
    }
}