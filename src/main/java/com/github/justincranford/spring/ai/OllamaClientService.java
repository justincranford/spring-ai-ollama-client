package com.github.justincranford.spring.ai;

import com.github.justincranford.spring.ai.config.OllamaClientServiceConfig;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.messages.AssistantMessage;
import org.springframework.ai.chat.model.ChatResponse;
import org.springframework.ai.chat.model.Generation;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.ai.ollama.OllamaChatModel;
import org.springframework.ai.ollama.api.OllamaApi;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;

import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.stream.Collectors;

import static java.util.Objects.requireNonNull;
import static java.util.concurrent.TimeUnit.SECONDS;

@Component
@RequiredArgsConstructor
@Slf4j
public class OllamaClientService {
	private static final int STREAM_TIMEOUT_SEC = 30;

	/**
	 * @see OllamaClientServiceConfig#ollamaChatModel(OllamaApi)
	 */
	private final OllamaChatModel ollamaChatModel;

	/**
	 * @see OllamaClientServiceConfig#chatClient(OllamaChatModel)
	 */
	private final ChatClient chatClient;

	/**
	 * Use ollamaChatModel in streaming mode. Use blocking to wait for all stream results. Piece the tokens together.
	 * @param prompt List of messages, with optional OllamaOptions
	 * @return The concatenated token contents.
	 * @see <a href="https://github.com/spring-projects/spring-ai/blob/6fc76b7f9bfcb032ac721ac8eaef0dab4a9386e2/models/spring-ai-openai/src/test/java/org/springframework/ai/openai/chat/proxy/OllamaWithOpenAiChatModelIT.java#L113"/>
	 */
	public String prompt1(final Prompt prompt) {
		final Flux<ChatResponse> chatResponseFlux = this.ollamaChatModel.stream(prompt);
		log.info("Prompt:\n{}", prompt);
		final List<ChatResponse> responses = requireNonNull(chatResponseFlux.collectList().block());
		final String response = responses.stream()
				.map(ChatResponse::getResults)
				.flatMap(List::stream)
				.map(Generation::getOutput)
				.map(AssistantMessage::getContent)
				.collect(Collectors.joining());
		log.info("Response:\n{}", response);
		return response;
	}

	/**
	 * Use ollamaChatModel in streaming mode. Use async flux handlers or timeout. Piece the tokens together.
	 * @param prompt List of messages, with optional OllamaOptions
	 * @return The concatenated token contents.
	 * @see <a href="https://github.com/spring-projects/spring-ai/blob/6fc76b7f9bfcb032ac721ac8eaef0dab4a9386e2/models/spring-ai-ollama/src/test/java/org/springframework/ai/ollama/OllamaChatModelIT.java#L204"/>
	 */
	public String prompt2(Prompt prompt) {
		try {
			log.info("Prompt:\n{}", prompt);
			final StringBuilder response = new StringBuilder(256);
			final CountDownLatch latch = new CountDownLatch(1);
			final Flux<ChatResponse> chatResponseFlux = this.chatClient.prompt(prompt).stream().chatResponse()
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
			final boolean success = latch.await(STREAM_TIMEOUT_SEC, SECONDS);
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
