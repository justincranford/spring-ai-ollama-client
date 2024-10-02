package com.github.justincranford.spring.ai;

import jakarta.annotation.Nonnull;
import org.springframework.ai.ollama.api.OllamaOptions;
import org.springframework.beans.factory.config.AbstractFactoryBean;

import java.security.SecureRandom;

public class OllamaOptionsFactory extends AbstractFactoryBean<OllamaOptions> {
    private static final SecureRandom SECURE_RANDOM = new SecureRandom();
    private static final String MODEL = "llama3.2";
    @Nonnull
    @Override
    public Class<?> getObjectType() {
        return OllamaOptions.class;
    }
    @Nonnull
    @Override
    protected OllamaOptions createInstance() {
        return OllamaOptions
                .create()
                .withModel(MODEL)
                .withKeepAlive("-1s")
                .withTemperature(SECURE_RANDOM.nextFloat(0.9f, 1f))
                .withSeed(SECURE_RANDOM.nextInt(0, Integer.MAX_VALUE))
                .withFormat("json");
    }
}
