package de.unistuttgart.iste.gits.common.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import de.unistuttgart.iste.gits.common.user_handling.User;
import lombok.SneakyThrows;
import org.springframework.context.annotation.Configuration;
import org.springframework.graphql.server.WebGraphQlInterceptor;
import org.springframework.graphql.server.WebGraphQlRequest;
import org.springframework.graphql.server.WebGraphQlResponse;
import reactor.core.publisher.Mono;

import java.util.Collections;

@Configuration
public class RequestHeaderUserInterceptor implements WebGraphQlInterceptor {
    @Override
    @SneakyThrows
    public Mono<WebGraphQlResponse> intercept(WebGraphQlRequest request, Chain chain) {
        String value = request.getHeaders().getFirst("CurrentUser");

        User currentUser = (new ObjectMapper()).readValue(value, User.class);

        request.configureExecutionInput(
                ((executionInput, builder) -> builder.graphQLContext(Collections.singletonMap(
                        "CurrentUser", currentUser
                )).build())
        );

        return chain.next(request);
    }
}