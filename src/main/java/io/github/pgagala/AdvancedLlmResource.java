package io.github.pgagala;

import jakarta.inject.Inject;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;

/**
 * REST resource for advanced LLM interactions
 */
@Path("/llm/advanced")
public class AdvancedLlmResource {

    @Inject
    AdvancedLangChainService advancedService;

    /**
     * Endpoint for chat with memory
     * 
     * @param request The chat request
     * @return Response with memory of previous interactions
     */
    @POST
    @Path("/chat-with-memory")
    @Produces(MediaType.APPLICATION_JSON)
    public ChatResponse chatWithMemory(ChatRequest request) {
        String response = advancedService.getChatResponse(request.message());
        return new ChatResponse(response);
    }
}