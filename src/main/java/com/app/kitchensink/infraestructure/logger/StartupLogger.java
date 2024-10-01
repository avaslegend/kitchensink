package com.app.kitchensink.infraestructure.logger;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

import com.app.kitchensink.application.service.PersonService;

@Component
public class StartupLogger implements CommandLineRunner {

    private static final Logger logger = LoggerFactory.getLogger(PersonService.class);

    private final Environment env;

    public StartupLogger(Environment env) {
        this.env = env;
    }

    @Override
    public void run(String... args) throws Exception {
        logger.info("Active profile(s): {}", String.join(", ", env.getActiveProfiles()));
        logger.info("Default profile(s): {}", String.join(", ", env.getDefaultProfiles()));
    }
}