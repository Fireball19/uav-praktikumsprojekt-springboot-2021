package de.hhu.propra.uav.domains.applicationservices.terminimporter;

import java.time.LocalDateTime;
import lombok.Data;

@Data
public class TerminFileDto {

  private final String tutor;

  private final LocalDateTime zeitpunkt;
}
