package de.hhu.propra.uav;

import java.util.stream.Stream;
import javax.sql.DataSource;
import org.springframework.beans.factory.config.BeanFactoryPostProcessor;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Profile;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.DatabaseStartupValidator;


@SuppressWarnings("PMD")
@SpringBootApplication
public class UebungsUndAnmeldungsverwaltungApplication {

  public static void main(String[] args) throws Exception {
    SpringApplication.run(UebungsUndAnmeldungsverwaltungApplication.class, args);
  }

  @Profile("!test")
  @Bean
  public DatabaseStartupValidator databaseStartupValidator(DataSource dataSource) {
    DatabaseStartupValidator databaseStartupValidator = new DatabaseStartupValidator();
    databaseStartupValidator.setDataSource(dataSource);
    return databaseStartupValidator;
  }

  @Profile("!test")
  @Bean
  public static BeanFactoryPostProcessor dependsOnPostProcessor() {
    return bf -> {
      String[] jdbc = bf.getBeanNamesForType(JdbcTemplate.class);
      Stream.of(jdbc)
          .map(bf::getBeanDefinition)
          .forEach(it -> it.setDependsOn("databaseStartupValidator"));
    };
  }


}
