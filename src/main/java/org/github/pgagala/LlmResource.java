package org.github.pgagala;

import jakarta.inject.Inject;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.apache.commons.lang3.StringUtils;

import java.util.concurrent.CompletionStage;
import java.util.concurrent.CompletableFuture;

/**
 * REST resource for LLM interactions
 */
@Path("/llm")
public class LlmResource {

    @Inject
    LangChainService langChainService;

    /**
     * Chat endpoint for sending prompts to the LLM
     * @param request The chat request containing the prompt
     * @return A CompletionStage with the LLM's response
     */
    @POST
    @Path("/chat")
    @Produces(MediaType.APPLICATION_JSON)
    public CompletionStage<Response> chat(ChatRequest request) {
        if (request == null || StringUtils.isBlank(request.message())) {
            return CompletableFuture.completedFuture(
                Response.status(Response.Status.BAD_REQUEST)
                    .entity(new ErrorResponse("Message cannot be empty"))
                    .build()
            );
        }

        return langChainService.generateResponse(request.message())
            .thenApply(response -> Response.ok(new ChatResponse(response)).build())
            .exceptionally(throwable -> 
                Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity(new ErrorResponse(throwable.getMessage()))
                    .build()
            );
    }

    /**
     * Chat request record
     */
    public record ChatRequest(String message) {
    }

    /**
     * Chat response record
     */
    public record ChatResponse(String response) {
    }

    /**
     * Error response record
     */
    public record ErrorResponse(String details) {}
}