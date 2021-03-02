package de.hhu.propra.uav.domains;

import java.time.LocalDateTime;
import java.util.List;

class Termin {
    private boolean reserviert;
    private Tutor tutor;
    private LocalDateTime zeitpunkt;
    private String name;
    private List<Student> studenten;
}
