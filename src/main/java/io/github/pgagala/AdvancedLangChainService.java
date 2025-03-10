package io.github.pgagala;

import dev.langchain4j.memory.ChatMemory;
import dev.langchain4j.memory.chat.MessageWindowChatMemory;
import dev.langchain4j.model.chat.ChatLanguageModel;
import dev.langchain4j.service.AiServices;
import dev.langchain4j.service.SystemMessage;
import dev.langchain4j.service.UserName;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

/**
 * Advanced LangChain4j Service that demonstrates more capabilities
 */
@ApplicationScoped
public class AdvancedLangChainService {

    private final AssistantWithMemory assistant;

    /**
     * Constructor with dependency injection
     * 
     * @param chatLanguageModel The LangChain4j chat model
     */
    @Inject
    public AdvancedLangChainService(ChatLanguageModel chatLanguageModel) {
        // Create an assistant with conversation memory
        ChatMemory chatMemory = MessageWindowChatMemory.builder()
                .maxMessages(10)
                .build();
        
        this.assistant = AiServices.builder(AssistantWithMemory.class)
                .chatLanguageModel(chatLanguageModel)
                .chatMemory(chatMemory)
                .build();
    }

    /**
     * Get response from an assistant that remembers conversation history
     * 
     * @param message User's message
     * @return Assistant's response
     */
    public String getChatResponse(String message) {
        return assistant.chat(message);
    }

    /**
     * Interface for a conversational assistant with memory
     */
    interface AssistantWithMemory {
        String chat(String userMessage);
    }
}