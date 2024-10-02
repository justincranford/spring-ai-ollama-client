package com.github.justincranford.spring.ai;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.ai.chat.messages.Message;
import org.springframework.ai.chat.messages.UserMessage;

import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.chat.prompt.ChatOptions;

/**
 *  Start: docker run --rm -d -v ollama:/root/.ollama -p 11434:11434 --name ollama ollama/ollama:latest
 *  Shell: docker exec -it ollama bash
 */
@Slf4j
public class SpringsServiceOllamaClientServiceIT extends AbstractIT {
	final List<Message> messages = List.of(
		new UserMessage("Why is the sky blue? Why is grass green?")
//		new SystemMessage("You are a good assistant, and must answer like Charles Darwin.")
	);

	@Test
	void defaultOllamaOptions() {
		helper(null);
	}

	@Test
	void overrideOllamaOptions() {
		helper(ollamaOptionsFactory().createInstance());
	}

	private void helper(final ChatOptions chatOptions) {
		final String response = ollamaClientService().prompt(this.messages, chatOptions);
		assertThat(response).isNotEmpty();
	}

	@Test
	void streamRoleTest() {
		final String response = ollamaClientService().prompt(this.messages);
		assertThat(response).isNotEmpty();
	}
}
