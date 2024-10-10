package com.niiazov.usermanagement.config;

import org.springframework.boot.SpringBootConfiguration;
import org.springframework.context.annotation.Profile;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootConfiguration
@EnableJpaAuditing
@Profile("prod")
public class AppConfig {
}
