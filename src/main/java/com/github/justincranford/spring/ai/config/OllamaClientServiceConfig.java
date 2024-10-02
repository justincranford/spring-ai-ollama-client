package com.github.justincranford.spring.ai.config;

import com.github.justincranford.spring.ai.OllamaClientService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.ollama.OllamaChatModel;
import org.springframework.ai.ollama.api.OllamaApi;
import org.springframework.ai.ollama.api.OllamaOptions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

@Configuration
@Import({OllamaOptionsFactory.class, OllamaClientService.class})
@SuppressWarnings({"unused"})
@RequiredArgsConstructor
@Slf4j
public class OllamaClientServiceConfig {
	/**
	 * @see OllamaOptionsFactory#createInstance()
	 */
	@Autowired
	final OllamaOptions ollamaOptions;

	@Bean
	public OllamaChatModel ollamaChatModel(final OllamaApi ollamaApi) {
		return new OllamaChatModel(ollamaApi, this.ollamaOptions);
	}

	@Bean
	public ChatClient chatClient(final OllamaChatModel ollamaChatModel) {
		return ChatClient.builder(ollamaChatModel).build();
	}
}
