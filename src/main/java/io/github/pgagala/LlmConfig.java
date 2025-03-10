package io.github.pgagala;

import dev.langchain4j.model.chat.ChatLanguageModel;
import dev.langchain4j.model.ollama.OllamaChatModel;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.inject.Produces;
import org.eclipse.microprofile.config.inject.ConfigProperty;

import java.time.Duration;

@ApplicationScoped
public class LlmConfig {

    @ConfigProperty(name = "llm.base.url", defaultValue = "http://localhost:11434")
    String baseUrl;

    @ConfigProperty(name = "llm.model.name", defaultValue = "llama3")
    String modelName;

    @ConfigProperty(name = "llm.temperature", defaultValue = "0.7")
    double temperature;

    @ConfigProperty(name = "llm.timeout.seconds", defaultValue = "30")
    int timeoutSeconds;

    @Produces
    @ApplicationScoped
    public ChatLanguageModel createChatLanguageModel() {
        return OllamaChatModel.builder()
                .baseUrl(baseUrl)
                .modelName(modelName)
                .temperature(temperature)
                .timeout(Duration.ofSeconds(timeoutSeconds))
                .build();
    }
} 