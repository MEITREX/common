package de.unistuttgart.iste.gits.common.user_handling;

import com.fasterxml.jackson.core.JacksonException;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.SneakyThrows;
import org.springframework.graphql.server.WebGraphQlInterceptor;
import org.springframework.graphql.server.WebGraphQlRequest;
import org.springframework.graphql.server.WebGraphQlResponse;
import org.springframework.web.bind.MissingRequestHeaderException;
import reactor.core.publisher.Mono;

import java.util.Collections;

/**
 * Simple helper class which can be used by WebGraphQlInterceptor implementations to extract the current user from the
 * request headers and add it to the GraphQL context.
 */
public class RequestHeaderUserProcessor {
    /**
     * Call this method from your WebGraphQlInterceptor implementation to extract the current user from the request and
     * add the user data to the graphql context as a LoggedInUser object living under the key "currentUser".
     * @param request The request to process.
     */
    @SneakyThrows
    public static void process(WebGraphQlRequest request) {
        if(!request.getHeaders().containsKey("CurrentUser"))
            return;

        String value = request.getHeaders().getFirst("CurrentUser");

        try {
            LoggedInUser currentUser = (new ObjectMapper()).readValue(value, LoggedInUser.class);

            request.configureExecutionInput(
                    ((executionInput, builder) -> builder.graphQLContext(Collections.singletonMap(
                            "currentUser", currentUser
                    )).build())
            );
        } catch(JacksonException ex) {
            // rethrow as a generic exception so we don't leak server-internal data to the client (json contents)
            throw new IllegalArgumentException("Invalid user data in request header.");
        }
    }
}