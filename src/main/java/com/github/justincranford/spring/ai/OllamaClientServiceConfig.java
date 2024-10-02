package com.github.justincranford.spring.ai;

import lombok.RequiredArgsConstructor;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.ollama.OllamaChatModel;
import org.springframework.ai.ollama.api.OllamaApi;
import org.springframework.ai.ollama.api.OllamaOptions;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Import;

@Configuration
@Import({OllamaOptionsFactory.class, OllamaClientService.class})
@SuppressWarnings({"unused"})
@RequiredArgsConstructor
@Slf4j
public class OllamaClientServiceConfig {
	private final OllamaOptionsFactory ollamaOptionsFactory;

	@Bean
	public OllamaChatModel getOllamaChatModel(OllamaApi ollamaApi, OllamaOptions ollamaOptions) {
		return new OllamaChatModel(ollamaApi, ollamaOptions);
	}

	@Bean
	public ChatClient chatClient(final OllamaChatModel ollamaChatModel) {
		return ChatClient.builder(ollamaChatModel).build();
	}
}
