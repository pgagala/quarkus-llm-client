package org.github.pgagala;

import jakarta.enterprise.context.ApplicationScoped;
import org.eclipse.microprofile.config.inject.ConfigProperty;
import org.eclipse.microprofile.faulttolerance.Retry;
import org.eclipse.microprofile.faulttolerance.Timeout;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.time.temporal.ChronoUnit;
import java.util.concurrent.CompletionStage;
import java.util.regex.Pattern;

/**
 * Service for communicating with a local LLM server
 */
@ApplicationScoped
public class LocalLlmService {

    private final HttpClient httpClient = HttpClient.newBuilder()
            .version(HttpClient.Version.HTTP_2)
            .connectTimeout(Duration.ofSeconds(10))
            .build();

    record LlmRequestBody(String model, String prompt, double temperature, int max_tokens, boolean stream) {}

    @ConfigProperty(name = "local.llm.url", defaultValue = "http://localhost:11434/api/generate")
    String localLlmUrl;

    @ConfigProperty(name = "llm.api.model", defaultValue = "codellama")
    String model;

    @ConfigProperty(name = "llm.api.temperature", defaultValue = "0.7")
    double temperature;

    @ConfigProperty(name = "llm.api.max-tokens", defaultValue = "10000000")
    int maxTokens;

    /**
     * Generates a response from the local LLM using the provided prompt
     * @param prompt The user's input text
     * @return A CompletionStage containing the LLM's response text
     */
    @Timeout(value = 30, unit = ChronoUnit.SECONDS)
    @Retry(maxRetries = 3, delay = 1, delayUnit = ChronoUnit.SECONDS)
    public CompletionStage<String> generateResponse(String prompt) {
        var requestBody = new LlmRequestBody(
            model,
            prompt.replace("\"", "\\\""),
            temperature,
            maxTokens,
            false
        );
        
        // Convert request body to JSON
        String jsonBody = """
                {
                    "model": "%s",
                    "prompt": "%s",
                    "temperature": %.1f,
                    "max_tokens": %d,
                    "stream": false
                }
                """.formatted(
                    requestBody.model(),
                    requestBody.prompt(),
                    requestBody.temperature(),
                    requestBody.max_tokens()
                );

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(localLlmUrl))
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(jsonBody))
                .build();

        return httpClient.sendAsync(request, HttpResponse.BodyHandlers.ofString())
                .thenApply(HttpResponse::body)
                .thenApply(this::parseLocalLlmResponse);
    }

    /**
     * Parses the JSON response from the LLM server
     * @param responseJson The JSON response from the LLM
     * @return The extracted response text
     */
    private String parseLocalLlmResponse(String responseJson) {
        // Pattern matching with guards
        if (responseJson.contains("\"response\":")) {
            // Using pattern matching for instanceof (Java 16+)
            var pattern = Pattern.compile("\"response\":\"(.*?)\"");
            var matcher = pattern.matcher(responseJson);
            
            if (matcher.find()) {
                return matcher.group(1);
            }
        }
        return "Unable to parse LLM response";
    }
}