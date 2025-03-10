package org.github.pgagala;

import java.util.List;

/**
 * Request model for LLM API interactions
 */
public record LlmRequest(
    String model,
    List<Message> messages,
    double temperature,
    int max_tokens
) {
    public record Message(String role, String content) {
    }
}