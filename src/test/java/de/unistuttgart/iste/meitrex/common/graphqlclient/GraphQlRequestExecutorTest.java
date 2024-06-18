package de.unistuttgart.iste.meitrex.common.graphqlclient;

import com.kobylynskyi.graphql.codegen.model.graphql.GraphQLOperation;
import com.kobylynskyi.graphql.codegen.model.graphql.GraphQLOperationRequest;
import com.kobylynskyi.graphql.codegen.model.graphql.GraphQLResponseProjection;
import graphql.GraphqlErrorException;
import org.jetbrains.annotations.NotNull;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.graphql.ResponseError;
import org.springframework.graphql.client.ClientGraphQlResponse;
import org.springframework.graphql.client.ClientResponseField;
import org.springframework.graphql.client.GraphQlClient;
import org.springframework.graphql.client.WebGraphQlClient;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.Map;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.notNullValue;
import static org.hamcrest.Matchers.sameInstance;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class GraphQlRequestExecutorTest {

    @Test
    void testStartBuildingRequest() {
        // Arrange
        WebGraphQlClient graphQlClient = mock(WebGraphQlClient.class);
        GraphQlRequestExecutor executor = new GraphQlRequestExecutor(() -> graphQlClient);
        GraphQLOperationRequest request = mock(GraphQLOperationRequest.class);

        // Act
        GraphQlRequestExecutor.OperationSpecification operationSpecification = executor.request(request);

        // Assert
        assertThat(operationSpecification, notNullValue());
    }

    @Test
    void testRetrieveResponse() {
        // Arrange
        GraphQLOperationRequest request = getMockRequest();
        GraphQLResponseProjection projection = getMockProjection();

        Object expectedResponse = new Object();
        ClientGraphQlResponse response = getMockResponse(expectedResponse);

        WebGraphQlClient graphQlClient = getMockGraphQlClient(response);

        GraphQlRequestExecutor executor = new GraphQlRequestExecutor(() -> graphQlClient);

        // Act
        Mono<Object> resultMono = executor.request(request)
                .projectTo(Object.class, projection)
                .retrieve();

        // Assert
        Object result = resultMono.block();
        assertThat(result, sameInstance(expectedResponse));

        // Verify
        Mockito.verify(graphQlClient).document(any(String.class));
    }

    @Test
    void testRetrieveListResponse() {
        // Arrange
        GraphQLOperationRequest request = getMockRequest();
        GraphQLResponseProjection projection = getMockProjection();

        List<Object> expectedResponse = List.of(new Object(), new Object());
        ClientGraphQlResponse response = getMockResponse(expectedResponse);

        WebGraphQlClient graphQlClient = getMockGraphQlClient(response);

        GraphQlRequestExecutor executor = new GraphQlRequestExecutor(() -> graphQlClient);

        // Act
        Mono<List<Object>> resultMono = executor.request(request)
                .projectTo(Object.class, projection)
                .retrieveList();

        // Assert
        List<Object> result = resultMono.block();
        assertThat(result, sameInstance(expectedResponse));

        // Verify
        Mockito.verify(graphQlClient).document(any(String.class));
    }

    @Test
    void testErrors() {
        // Arrange
        GraphQLOperationRequest request = getMockRequest();
        GraphQLResponseProjection projection = getMockProjection();

        ClientGraphQlResponse response = getMockResponse(null, getMockError());

        WebGraphQlClient graphQlClient = getMockGraphQlClient(response);

        GraphQlRequestExecutor executor = new GraphQlRequestExecutor(() -> graphQlClient);

        // Act
        Mono<Object> resultMono = executor.request(request)
                .projectTo(Object.class, projection)
                .retrieve();

        assertThrows(GraphqlErrorException.class, resultMono::block);
    }

    private static @NotNull ResponseError getMockError() {
        ResponseError error = mock(ResponseError.class);
        when(error.getMessage()).thenReturn("error message");
        return error;
    }

    private static @NotNull WebGraphQlClient getMockGraphQlClient(ClientGraphQlResponse response) {
        WebGraphQlClient graphQlClient = mock(WebGraphQlClient.class);
        GraphQlClient.RequestSpec requestSpec = mock(GraphQlClient.RequestSpec.class);
        when(graphQlClient.document(Mockito.anyString())).thenReturn(requestSpec);
        when(requestSpec.execute()).thenReturn(Mono.just(response));
        return graphQlClient;
    }

    private static @NotNull ClientGraphQlResponse getMockResponse(Object expectedResponse,
                                                                  ResponseError... errors) {
        ClientGraphQlResponse response = mock(ClientGraphQlResponse.class);
        when(response.getErrors()).thenReturn(List.of(errors));
        ClientResponseField expectedField = mock(ClientResponseField.class);
        when(response.field(any())).thenReturn(expectedField);
        when(expectedField.toEntity(Object.class)).thenReturn(expectedResponse);
        return response;
    }

    private static @NotNull ClientGraphQlResponse getMockResponse(List<Object> expectedResponse) {
        ClientGraphQlResponse response = mock(ClientGraphQlResponse.class);
        ClientResponseField expectedField = mock(ClientResponseField.class);
        when(response.field(any())).thenReturn(expectedField);
        when(expectedField.toEntityList(Object.class)).thenReturn(expectedResponse);
        return response;
    }

    private static @NotNull GraphQLResponseProjection getMockProjection() {
        GraphQLResponseProjection projection = mock(GraphQLResponseProjection.class);
        when(projection.toString()).thenReturn("projection");
        return projection;
    }

    private static @NotNull GraphQLOperationRequest getMockRequest() {
        GraphQLOperationRequest request = mock(GraphQLOperationRequest.class);
        when(request.getOperationName()).thenReturn("operationName");
        when(request.getOperationType()).thenReturn(GraphQLOperation.QUERY);
        when(request.getInput()).thenReturn(Map.of());
        return request;
    }
}
