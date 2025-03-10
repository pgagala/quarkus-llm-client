package org.github.pgagala;

import dev.langchain4j.data.message.AiMessage;
import dev.langchain4j.data.message.UserMessage;
import dev.langchain4j.model.chat.ChatLanguageModel;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import org.eclipse.microprofile.faulttolerance.Retry;
import org.eclipse.microprofile.faulttolerance.Timeout;

import java.time.temporal.ChronoUnit;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionStage;

/**
 * Service for handling LLM interactions using LangChain4j
 */
@ApplicationScoped
public class LangChainService {

    private final ChatLanguageModel chatLanguageModel;

    @Inject
    public LangChainService(ChatLanguageModel chatLanguageModel) {
        this.chatLanguageModel = chatLanguageModel;
    }

    /**
     * Generates a response from the LLM using LangChain4j
     * 
     * @param prompt The user's input text
     * @return A CompletionStage containing the LLM's response text
     */
    @Timeout(value = 30, unit = ChronoUnit.SECONDS)
    @Retry(maxRetries = 3, delay = 1, delayUnit = ChronoUnit.SECONDS)
    public CompletionStage<String> generateResponse(String prompt) {
        return CompletableFuture.supplyAsync(() -> {
            UserMessage userMessage = UserMessage.from(prompt);
            AiMessage aiMessage = chatLanguageModel.generate(userMessage).content();
            return aiMessage.text();
        });
    }
}