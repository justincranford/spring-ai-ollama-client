package com.github.justincranford.spring.ai;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.ClassOrderer;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestClassOrder;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.ai.chat.messages.Message;
import org.springframework.ai.chat.messages.UserMessage;
import org.springframework.ai.chat.prompt.Prompt;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@TestClassOrder(ClassOrderer.OrderAnnotation.class)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@Slf4j
public class OllamaClientServiceIT extends AbstractIT {
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
			final Prompt prompt = new Prompt(OllamaClientServiceIT.this.messages);
			final String response = ollamaClientService().prompt1(prompt);
			assertThat(response).isNotEmpty();
		}

		@Order(2)
		@Test
		void prompt1_overrideOllamaOptions() {
			final Prompt prompt = new Prompt(OllamaClientServiceIT.this.messages, ollamaOptionsFactory().createInstance());
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
			final Prompt prompt = new Prompt(OllamaClientServiceIT.this.messages);
			final String response = ollamaClientService().prompt2(prompt);
			assertThat(response).isNotEmpty();
		}

		@Order(2)
		@Test
		void prompt2_overrideOllamaOptions() {
			final Prompt prompt = new Prompt(OllamaClientServiceIT.this.messages, ollamaOptionsFactory().createInstance());
			final String response = ollamaClientService().prompt2(prompt);
			assertThat(response).isNotEmpty();
		}
	}
}
