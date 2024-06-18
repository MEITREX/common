package de.unistuttgart.iste.meitrex.common.user_handling;

import org.junit.jupiter.api.Test;
import org.springframework.graphql.server.WebGraphQlRequest;
import org.springframework.http.HttpHeaders;

import java.net.URI;
import java.time.OffsetDateTime;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class RequestHeaderUserProcessorTest {

    @Test
    void testUserDataContextInjection() {
        final String json = """
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
                    ],
                    "realmRoles": []
                }
                """;

        final LoggedInUser.CourseMembership courseMembership = LoggedInUser.CourseMembership.builder()
                .courseId(java.util.UUID.fromString("123e4567-e89b-12d3-a456-426614174000"))
                .published(true)
                .role(LoggedInUser.UserRoleInCourse.STUDENT)
                .startDate(OffsetDateTime.parse("2020-01-01T00:00:00.000Z"))
                .endDate(OffsetDateTime.parse("2021-01-01T00:00:00.000Z"))
                .build();


        final LoggedInUser user = LoggedInUser.builder()
                .id(java.util.UUID.fromString("123e4567-e89b-12d3-a456-426614174000"))
                .userName("MyUserName")
                .firstName("John")
                .lastName("Doe")
                .courseMemberships(List.of(courseMembership))
                .realmRoles(new HashSet<>())
                .build();


        final HttpHeaders headers = new HttpHeaders();
        headers.add("CurrentUser", json);

        // ugly way of instantiating a WebGraphQlRequest. We have to add a key-value pair with query as a key to the
        // body so the constructor of WebGraphQlRequest doesn't complain.
        final WebGraphQlRequest request = new WebGraphQlRequest(
                URI.create(""), headers, null, null, Map.of(), Map.of("query", "a"), "", null);

        RequestHeaderUserProcessor.process(request);

        assertThat((LoggedInUser)request.toExecutionInput().getGraphQLContext().get("currentUser"))
                .usingRecursiveComparison()
                .isEqualTo(user);
    }

    @Test
    void testNoUserDataContextInjection() {
        final HttpHeaders headers = new HttpHeaders();

        // ugly way of instantiating a WebGraphQlRequest. We have to add a key-value pair with query as a key to the
        // body so the constructor of WebGraphQlRequest doesn't complain.
        final WebGraphQlRequest request = new WebGraphQlRequest(
                URI.create(""), headers, null, null, Map.of(), Map.of("query", "a"), "", null);

        RequestHeaderUserProcessor.process(request);

        assertThat(request.toExecutionInput().getGraphQLContext().hasKey("currentUser")).isFalse();
    }

    @Test
    void testInvalidUserDataContextInjection() {
        final String json = """
                {
                    "id1": "123e4567-e89b-12d3-a456-426614174000",
                    "userName": "MyUserName",
                    "firstName": "John",
                    "lastName": "Doe"
                }
                """;

        final HttpHeaders headers = new HttpHeaders();
        headers.add("CurrentUser", json);

        // ugly way of instantiating a WebGraphQlRequest. We have to add a key-value pair with query as a key to the
        // body so the constructor of WebGraphQlRequest doesn't complain.
        final WebGraphQlRequest request = new WebGraphQlRequest(
                URI.create(""), headers, null, null, Map.of(), Map.of("query", "a"), "", null);

        assertThatThrownBy(() -> RequestHeaderUserProcessor.process(request))
                .isInstanceOf(IllegalArgumentException.class);
    }
}
