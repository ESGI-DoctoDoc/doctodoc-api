package fr.esgi.doctodocapi.configuration;

import org.springdoc.core.models.GroupedOpenApi;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {
    @Bean
    public GroupedOpenApi doctodocApi() {
        return GroupedOpenApi.builder()
                .group("doctodoc")
                .packagesToScan("fr.esgi.doctodocapi.presentation")
                .pathsToMatch("/api/v1/**")
                .build();
    }
}
