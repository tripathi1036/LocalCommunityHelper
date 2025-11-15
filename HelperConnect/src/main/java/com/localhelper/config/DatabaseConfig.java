package com.localhelper.config;

import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@Configuration
@EnableJpaRepositories(basePackages = "com.localhelper.repository")
@EntityScan(basePackages = "com.localhelper.entity")
@EnableJpaAuditing
@EnableTransactionManagement
public class DatabaseConfig {
    // This class enables JPA configuration and auditing
}