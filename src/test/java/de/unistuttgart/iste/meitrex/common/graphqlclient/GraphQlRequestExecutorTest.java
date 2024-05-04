package de.unistuttgart.iste.meitrex.common.graphqlclient;

import com.kobylynskyi.graphql.codegen.model.graphql.GraphQLOperation;
import com.kobylynskyi.graphql.codegen.model.graphql.GraphQLOperationRequest;
import com.kobylynskyi.graphql.codegen.model.graphql.GraphQLResponseProjection;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
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
        WebGraphQlClient graphQlClient = mock(WebGraphQlClient.class);
        GraphQlRequestExecutor executor = new GraphQlRequestExecutor(() -> graphQlClient);

        GraphQLOperationRequest request = mock(GraphQLOperationRequest.class);
        when(request.getOperationName()).thenReturn("operationName");
        when(request.getOperationType()).thenReturn(GraphQLOperation.QUERY);
        when(request.getInput()).thenReturn(Map.of());
        Class<Object> responseType = Object.class;

        GraphQLResponseProjection projection = mock(GraphQLResponseProjection.class);
        when(projection.toString()).thenReturn("projection");

        GraphQlClient.RequestSpec requestSpec = mock(GraphQlClient.RequestSpec.class);
        when(graphQlClient.document(Mockito.anyString())).thenReturn(requestSpec);

        ClientGraphQlResponse response = mock(ClientGraphQlResponse.class);
        when(response.getErrors()).thenReturn(List.of());

        Object expectedResponse = new Object();
        ClientResponseField expectedField = mock(ClientResponseField.class);
        when(expectedField.toEntity(Object.class)).thenReturn(expectedResponse);

        when(requestSpec.execute()).thenReturn(Mono.just(response));
        when(response.field(any())).thenReturn(expectedField);

        // Act
        Mono<Object> resultMono = executor.request(request)
                .projectTo(responseType, projection)
                .retrieve();

        // Assert
        Object result = resultMono.block();
        assertThat(result, sameInstance(expectedResponse));

        // Verify
        Mockito.verify(graphQlClient).document(any(String.class));
        Mockito.verify(requestSpec).execute();
    }
}
