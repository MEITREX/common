package de.unistuttgart.iste.gits.common.user_handling;

import org.junit.jupiter.api.Test;
import org.springframework.graphql.server.WebGraphQlRequest;
import org.springframework.http.HttpHeaders;
import java.net.URI;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

public class RequestHeaderUserProcessorTest {
    @Test
    public void testUserDataContextInjection() {
        String json = """
                {
                    "id": "123e4567-e89b-12d3-a456-426614174000",
                    "userName": "MyUserName",
                    "firstName": "John",
                    "lastName": "Doe",
                    "authToken": "this isn't an auth token"
                }
                """;

        LoggedInUser user = new LoggedInUser(
            java.util.UUID.fromString("123e4567-e89b-12d3-a456-426614174000"),
            "MyUserName",
            "John",
            "Doe",
                "this isn't an auth token"
        );

        HttpHeaders headers = new HttpHeaders();
        headers.add("CurrentUser", json);

        // ugly way of instantiating a WebGraphQlRequest. We have to add a key-value pair with query as a key to the
        // body so the constructor of WebGraphQlRequest doesn't complain.
        WebGraphQlRequest request = new WebGraphQlRequest(
                URI.create(""), headers, null, Map.of(), Map.of("query", "a"), "", null);

        RequestHeaderUserProcessor.process(request);

        assertThat((LoggedInUser)request.toExecutionInput().getGraphQLContext().get("currentUser"))
                .usingRecursiveComparison()
                .isEqualTo(user);
    }

    @Test
    public void testNoUserDataContextInjection() {
        HttpHeaders headers = new HttpHeaders();

        // ugly way of instantiating a WebGraphQlRequest. We have to add a key-value pair with query as a key to the
        // body so the constructor of WebGraphQlRequest doesn't complain.
        WebGraphQlRequest request = new WebGraphQlRequest(
                URI.create(""), headers, null, Map.of(), Map.of("query", "a"), "", null);

        RequestHeaderUserProcessor.process(request);

        assertThat(request.toExecutionInput().getGraphQLContext().hasKey("currentUser")).isFalse();
    }

    @Test
    public void testInvalidUserDataContextInjection() {
        String json = """
                {
                    "id1": "123e4567-e89b-12d3-a456-426614174000",
                    "userName": "MyUserName",
                    "firstName": "John",
                    "lastName": "Doe"
                }
                """;

        HttpHeaders headers = new HttpHeaders();
        headers.add("CurrentUser", json);

        // ugly way of instantiating a WebGraphQlRequest. We have to add a key-value pair with query as a key to the
        // body so the constructor of WebGraphQlRequest doesn't complain.
        WebGraphQlRequest request = new WebGraphQlRequest(
                URI.create(""), headers, null, Map.of(), Map.of("query", "a"), "", null);

        assertThatThrownBy(() -> RequestHeaderUserProcessor.process(request))
                .isInstanceOf(IllegalArgumentException.class);
    }
}
