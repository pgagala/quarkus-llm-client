package org.github.pgagala;

import java.util.List;

/**
 * Response model for LLM API responses
 */
public record LlmResponse(
    String id,
    String object,
    long created,
    String model,
    List<Choice> choices,
    Usage usage
) {
    /**
     * Default constructor required for JSON deserialization
     */
    public LlmResponse() {
        this(null, null, 0, null, List.of(), null);
    }

    /**
     * Choice record representing a single completion response
     */
    public record Choice(
        int index,
        LlmRequest.Message message,
        String finish_reason
    ) {
        /**
         * Default constructor required for JSON deserialization
         */
        public Choice() {
            this(0, null, null);
        }
    }

    /**
     * Usage record for token consumption metrics
     */
    public record Usage(
        int prompt_tokens,
        int completion_tokens,
        int total_tokens
    ) {
        /**
         * Default constructor required for JSON deserialization
         */
        public Usage() {
            this(0, 0, 0);
        }
    }
}