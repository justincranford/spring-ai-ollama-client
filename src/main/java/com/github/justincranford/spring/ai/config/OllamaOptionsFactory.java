package com.github.justincranford.spring.ai.config;

import jakarta.annotation.Nonnull;
import org.springframework.ai.ollama.api.OllamaOptions;
import org.springframework.beans.factory.config.AbstractFactoryBean;

import java.security.SecureRandom;

public class OllamaOptionsFactory extends AbstractFactoryBean<OllamaOptions> {
    private static final SecureRandom SECURE_RANDOM = new SecureRandom();
    private static final String MODEL = "llama3.2";
    private static final String KEEP_ALIVE = "-1s";

    @Nonnull
    @Override
    public OllamaOptions createInstance() {
        return OllamaOptions.create().withModel(MODEL).withKeepAlive(KEEP_ALIVE)
            .withTemperature(SECURE_RANDOM.nextFloat(0.9f, 1f)) // range 0-1 (inclusive)
            .withSeed(SECURE_RANDOM.nextInt(0, Integer.MAX_VALUE));
    }

    @Nonnull
    @Override
    public Class<?> getObjectType() {
        return OllamaOptions.class;
    }
}
