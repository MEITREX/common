package de.unistuttgart.iste.gits.common.user_handling;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.SneakyThrows;
import org.springframework.graphql.server.WebGraphQlInterceptor;
import org.springframework.graphql.server.WebGraphQlRequest;
import org.springframework.graphql.server.WebGraphQlResponse;
import reactor.core.publisher.Mono;

import java.util.Collections;

/**
 * Simple helper class which can be used by WebGraphQlInterceptor implementations to extract the current user from the
 * request headers and add it to the GraphQL context.
 */
public class RequestHeaderUserProcessor {
    /**
     * Call this method from your WebGraphQlInterceptor implementation to extract the current user from the request.
     * @param request The request to process.
     * @param chain The chain to continue the processing.
     * @return The response.
     */
    @SneakyThrows
    public static Mono<WebGraphQlResponse> process(WebGraphQlRequest request, WebGraphQlInterceptor.Chain chain) {
        String value = request.getHeaders().getFirst("CurrentUser");

        LoggedInUser currentUser = (new ObjectMapper()).readValue(value, LoggedInUser.class);

        request.configureExecutionInput(
                ((executionInput, builder) -> builder.graphQLContext(Collections.singletonMap(
                        "currentUser", currentUser
                )).build())
        );

        return chain.next(request);
    }
}