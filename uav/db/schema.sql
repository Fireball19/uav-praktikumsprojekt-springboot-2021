CREATE DATABASE IF NOT EXISTS uavdb;
USE uavdb;
DROP TABLE IF EXISTS termin_student;
DROP TABLE IF EXISTS termin;
DROP TABLE IF EXISTS student;
DROP TABLE IF EXISTS uebung;

CREATE TABLE IF NOT EXISTS uebung
(
    id             BIGINT AUTO_INCREMENT,
    name           VARCHAR(100),
    modus          VARCHAR(20),
    min_groesse    INTEGER,
    max_groesse    INTEGER,
    anmeldebeginn  DATETIME,
    anmeldeschluss DATETIME,
    PRIMARY KEY(id)
);

CREATE TABLE IF NOT EXISTS termin
(
  id          BIGINT AUTO_INCREMENT,
  zeitpunkt   DATETIME,
  reserviert  BOOLEAN,
  min_groesse INTEGER,
  max_groesse INTEGER,
  tutor       VARCHAR(50),
  uebung_key  BIGINT,
  uebung      BIGINT,
  PRIMARY KEY (id),
  CONSTRAINT fk_termin_uebung FOREIGN KEY (uebung) REFERENCES uebung (id)
);

CREATE TABLE IF NOT EXISTS student
(
    github VARCHAR(50) PRIMARY KEY
);

CREATE TABLE IF NOT EXISTS termin_student
(
    termin_id      BIGINT,
    student_github VARCHAR(50),
    PRIMARY KEY (termin_id, student_github)
);