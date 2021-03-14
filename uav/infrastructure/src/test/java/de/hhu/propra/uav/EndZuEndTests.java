package de.hhu.propra.uav;


import de.hhu.propra.uav.domains.services.AnmeldungService;
import de.hhu.propra.uav.domains.services.StudentService;
import de.hhu.propra.uav.domains.services.UebungService;
import de.hhu.propra.uav.domains.services.VerwaltungService;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;

import java.io.IOException;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class EndZuEndTests {

  @Autowired
  TestRestTemplate testRestTemplate;

  @Autowired
  VerwaltungService verwalgService;
  @Autowired
  AnmeldungService anmeldungService;
  @Autowired
  StudentService studentService;
  @Autowired
  UebungService uebungService;

  /*
  @BeforeAll
  public static void dockerSetUp() throws IOException {
    Runtime.getRuntime().exec("cd..");
    String[] cmd = {"/bin/sh", "-c", "cd.."};

    Runtime.getRuntime().exec("docker-compose -f docker-compose-test.yml down");
    Runtime.getRuntime().exec("docker-compose -f docker-compose-test.yml up");
  }

   */

  @Order(1)
  @Test
  public void initializeTest(){
    System.out.println(1);
    assertThat(uebungService.findAll().size()).isEqualTo(2);
    assertThat(studentService.findAll().size()).isEqualTo(6);
  }

  @Order(2)
  @Test
  public void addUebungTest(){
    System.out.println(2);
    studentService.addStudent("Test111");

    assertThat(studentService.findAll().size()).isEqualTo(7);

  }

}
