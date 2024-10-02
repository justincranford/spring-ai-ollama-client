package com.github.justincranford.spring.ai;

import com.github.justincranford.spring.ai.config.OllamaClientServiceConfig;
import com.github.justincranford.spring.ai.config.OllamaOptionsFactory;
import lombok.Getter;
import lombok.experimental.Accessors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE, classes={ OllamaClientServiceConfig.class })
@EnableAutoConfiguration
@Getter
@Accessors(fluent = true)
@ActiveProfiles({"test"})
public abstract class AbstractIT {
	/**
	 * @see OllamaClientService
	 */
	@Autowired
	private OllamaClientService ollamaClientService;

	/**
	 * @see OllamaOptionsFactory#createInstance()
	 */
	@Autowired
	private OllamaOptionsFactory ollamaOptionsFactory;
}
