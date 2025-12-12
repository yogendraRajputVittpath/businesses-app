package com.user.business.config;

import javax.sql.DataSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.datasource.DriverManagerDataSource;

import com.user.business.service.util.Encryptor;

@Configuration
public class ManualDataSourceConfig {

    private static final Logger log = LoggerFactory.getLogger(ManualDataSourceConfig.class);

    @Value("${spring.datasource.url}")
    private String dbUrl;

    @Value("${spring.datasource.username}")
    private String dbUsername;

    @Value("${spring.datasource.password}")
    private String encryptedDbPassword;

    @Bean
    public DataSource dataSource() {
        log.info("Initializing manual DataSource...");

        // Use Encryptor utility for decryption
        String decryptedPassword = Encryptor.decrypt(encryptedDbPassword);

        DriverManagerDataSource dataSource = new DriverManagerDataSource();
        dataSource.setUrl(dbUrl);
        dataSource.setUsername(dbUsername);
        dataSource.setPassword(decryptedPassword);

        log.info("DataSource created successfully using decrypted password");
        return dataSource;
    }
}

