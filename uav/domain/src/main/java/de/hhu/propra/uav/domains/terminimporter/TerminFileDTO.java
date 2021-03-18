package de.hhu.propra.uav.domains.terminimporter;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class TerminFileDTO {

  private final String tutor;

  private final LocalDateTime zeitpunkt;
}
