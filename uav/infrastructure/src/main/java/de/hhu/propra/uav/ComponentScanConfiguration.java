package de.hhu.propra.uav;

import static org.springframework.context.annotation.FilterType.ANNOTATION;

import de.hhu.propra.uav.domains.annotations.ApplicationService;
import de.hhu.propra.uav.domains.annotations.DomainService;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

@SuppressWarnings("PMD")
@Configuration
@Profile("!test")
@ComponentScan(
    includeFilters = {
        @ComponentScan.Filter(type = ANNOTATION, classes = ApplicationService.class),
        @ComponentScan.Filter(type = ANNOTATION, classes = DomainService.class)
    }
)
public class ComponentScanConfiguration {
}
