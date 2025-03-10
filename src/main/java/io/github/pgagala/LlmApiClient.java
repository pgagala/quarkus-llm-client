package io.github.pgagala;

import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import org.eclipse.microprofile.rest.client.inject.RegisterRestClient;

import java.util.concurrent.CompletionStage;

@Path("/v1")
@RegisterRestClient(configKey = "llm-api")
public interface LlmApiClient {

    @POST
    @Path("/chat/completions")
    CompletionStage<LlmResponse> generateCompletion(LlmRequest request);
}