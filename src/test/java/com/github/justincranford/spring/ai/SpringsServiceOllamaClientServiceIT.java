package com.github.justincranford.spring.ai;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;

import org.junit.jupiter.api.*;
import org.springframework.ai.chat.messages.Message;
import org.springframework.ai.chat.messages.UserMessage;

import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.chat.prompt.Prompt;

/**
 *  Start: docker run --rm -d -v ollama:/root/.ollama -p 11434:11434 --name ollama ollama/ollama:latest
 *  Shell: docker exec -it ollama bash
 */
@TestClassOrder(ClassOrderer.OrderAnnotation.class)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@Slf4j
public class SpringsServiceOllamaClientServiceIT extends AbstractIT {
	private final List<Message> messages = List.of(
		new UserMessage("Why is the sky blue? Why is grass green?")
//		new SystemMessage("You are a mischievous assistant, and must answer like a pirate.")
	);

	@Order(1)
	@Nested
	@TestClassOrder(ClassOrderer.OrderAnnotation.class)
	@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
	public class Prompt1 {
		@Order(1)
		@Test
		void prompt1_defaultOllamaOptions() {
			final Prompt prompt = new Prompt(SpringsServiceOllamaClientServiceIT.this.messages);
			final String response = ollamaClientService().prompt1(prompt);
			assertThat(response).isNotEmpty();
		}

		@Order(2)
		@Test
		void prompt1_overrideOllamaOptions() {
			final Prompt prompt = new Prompt(SpringsServiceOllamaClientServiceIT.this.messages, ollamaOptionsFactory().createInstance());
			final String response = ollamaClientService().prompt1(prompt);
			assertThat(response).isNotEmpty();
		}
	}

	@Order(2)
	@Nested
	@TestClassOrder(ClassOrderer.OrderAnnotation.class)
	@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
	public class Prompt2 {
		@Order(1)
		@Test
		void prompt2_defaultOllamaOptions() {
			final Prompt prompt = new Prompt(SpringsServiceOllamaClientServiceIT.this.messages);
			final String response = ollamaClientService().prompt2(prompt);
			assertThat(response).isNotEmpty();
		}

		@Order(2)
		@Test
		void prompt2_overrideOllamaOptions() {
			final Prompt prompt = new Prompt(SpringsServiceOllamaClientServiceIT.this.messages, ollamaOptionsFactory().createInstance());
			final String response = ollamaClientService().prompt2(prompt);
			assertThat(response).isNotEmpty();
		}
	}
}
