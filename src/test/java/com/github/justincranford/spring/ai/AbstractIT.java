package com.github.justincranford.spring.ai;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.ollama.OllamaChatModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import lombok.Getter;
import lombok.experimental.Accessors;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE, classes={ OllamaClientServiceConfig.class })
@EnableAutoConfiguration
@Getter
@Accessors(fluent = true)
@ActiveProfiles({"test"})
@SuppressWarnings({"unused"})
public abstract class AbstractIT {
	@Autowired
	private OllamaOptionsFactory ollamaOptionsFactory;

	@Autowired
	private OllamaClientService ollamaClientService;

	@Autowired
	private OllamaChatModel chatModel;

	@Autowired
	private ChatClient chatClient;
}
