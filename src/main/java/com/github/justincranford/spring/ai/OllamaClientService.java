package com.github.justincranford.spring.ai;

import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.messages.AssistantMessage;
import org.springframework.ai.chat.messages.Message;
import org.springframework.ai.chat.model.ChatResponse;
import org.springframework.ai.chat.model.Generation;
import org.springframework.ai.chat.prompt.ChatOptions;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.ai.ollama.OllamaChatModel;
import org.springframework.stereotype.Component;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Flux;

import static java.util.Objects.requireNonNull;

/**
 * Examples:
 * <a href="https://github.com/spring-projects/spring-ai/blob/6fc76b7f9bfcb032ac721ac8eaef0dab4a9386e2/models/spring-ai-ollama/src/test/java/org/springframework/ai/ollama/OllamaChatModelIT.java#L204"/>
 * <a href="https://github.com/spring-projects/spring-ai/blob/6fc76b7f9bfcb032ac721ac8eaef0dab4a9386e2/models/spring-ai-openai/src/test/java/org/springframework/ai/openai/chat/proxy/OllamaWithOpenAiChatModelIT.java#L113"/>
 */
@Component
@RequiredArgsConstructor
@Slf4j
public class OllamaClientService {
	private final OllamaChatModel chatModel;
	private final ChatClient chatClient;

	public String prompt(final List<Message> messages) {
		final Prompt request = new Prompt(messages);
		log.info("Request:\n{}", request);
		final Flux<ChatResponse> flux = this.chatModel.stream(request);
		final List<ChatResponse> responses = requireNonNull(flux.collectList().block());
		final String response = responses.stream()
				.map(ChatResponse::getResults)
				.flatMap(List::stream)
				.map(Generation::getOutput)
				.map(AssistantMessage::getContent)
				.collect(Collectors.joining());
		log.info("Response:\n{}", response);
		return response;
	}

	public String prompt(final List<Message> messages, final ChatOptions chatOptions) {
		try {
			final Prompt request = (chatOptions == null) ? new Prompt(messages) : new Prompt(messages, chatOptions);
	        log.info("Request:\n{}", request);
			final StringBuilder response = new StringBuilder();
			final CountDownLatch latch = new CountDownLatch(1);
			Flux<ChatResponse> chatResponseFlux = this.chatClient.prompt(request).stream().chatResponse()
				.doOnNext(chatResponse -> {
					final Generation result = chatResponse.getResult();
					response.append(result.getOutput().getContent());
					final String finishReason = result.getMetadata().getFinishReason();
					if (finishReason != null) {
						log.info("finishReason:\n{}", finishReason);
					}
				})
				.doOnComplete(() -> {
					log.info("Response:\n{}", response);
					latch.countDown();
				});
			chatResponseFlux.subscribe();
			final boolean success = latch.await(20, TimeUnit.SECONDS);
			if (!success) {
				log.error("Timed out:\n{}", response);
				throw new RuntimeException("Timed out");
			}
			return response.toString();
		} catch(Exception e) {
	        log.info("Exception: ", e);
	        throw new RuntimeException(e);
		}
	}
}
