package de.unistuttgart.iste.gits.common.user_handling;

import org.junit.jupiter.api.Test;
import org.springframework.graphql.server.WebGraphQlRequest;
import org.springframework.http.HttpHeaders;
import java.net.URI;
import java.time.OffsetDateTime;
import java.util.List;
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
                    "courseMemberships": [
                        {
                            "courseId": "123e4567-e89b-12d3-a456-426614174000",
                            "role": "STUDENT",
                            "published": true,
                            "startDate": "2020-01-01T00:00:00.000Z",
                            "endDate": "2021-01-01T00:00:00.000Z"
                        }
                    ]
                }
                """;

        LoggedInUser user = new LoggedInUser(
                java.util.UUID.fromString("123e4567-e89b-12d3-a456-426614174000"),
                "MyUserName",
                "John",
                "Doe",
                List.of(new LoggedInUser.CourseMembership(
                        java.util.UUID.fromString("123e4567-e89b-12d3-a456-426614174000"),
                        LoggedInUser.UserRoleInCourse.STUDENT,
                        true,
                        OffsetDateTime.parse("2020-01-01T00:00:00.000Z"),
                        OffsetDateTime.parse("2021-01-01T00:00:00.000Z"))
                )
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
