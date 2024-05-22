package de.unistuttgart.iste.meitrex.common.graphqlclient;

import com.kobylynskyi.graphql.codegen.model.graphql.GraphQLOperationRequest;
import com.kobylynskyi.graphql.codegen.model.graphql.GraphQLRequest;
import com.kobylynskyi.graphql.codegen.model.graphql.GraphQLResponseProjection;
import graphql.ErrorType;
import graphql.GraphqlErrorException;
import lombok.RequiredArgsConstructor;
import org.springframework.graphql.ResponseError;
import org.springframework.graphql.client.ClientGraphQlResponse;
import org.springframework.graphql.client.WebGraphQlClient;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.Optional;
import java.util.function.Supplier;

/**
 * Executes GraphQL requests, using generated client classes from the GraphQL codegen plugin.
 * <p>
 * An example how to build requests and projections:
 *
 * <pre>
 * GraphQlRequestExecutor executor = new GraphQlRequestExecutor(graphQlClient, authTokenSupplier);
 * ProductByIdQueryRequest productByIdQueryRequest = new ProductByIdQueryRequest();
 * productByIdQueryRequest.setId(productId);
 * ProductResponseProjection responseProjection = new ProductResponseProjection()
 *                 .id()
 *                 .title()
 *                 .price();
 *
 * executor.request(productByIdQueryRequest)
 *                 .projectTo(Product.class, responseProjection)
 *                 .retrieve()
 *                 .subscribe(product -> // do something with the product);
 * </pre>
 */
public class GraphQlRequestExecutor {

    private final Supplier<WebGraphQlClient> graphQlClientSupplier;

    /**
     * Create a new request executor.
     *
     * @param graphQlClientSupplier The supplier for the client to use for the requests
     *                              This is a supplier to allow adding headers to the client for each request.
     */
    public GraphQlRequestExecutor(Supplier<WebGraphQlClient> graphQlClientSupplier) {
        this.graphQlClientSupplier = graphQlClientSupplier;
    }

    /**
     * Start building a new request.
     *
     * @param request The request object to build
     * @return The builder for the request
     */
    public OperationSpecification request(GraphQLOperationRequest request) {
        return new OperationSpecification(request);
    }

    /**
     * Internal class to build a request and projection.
     */
    @RequiredArgsConstructor
    public class OperationSpecification {
        private final GraphQLOperationRequest request;

        /**
         * Specify the result type and projection for the request.
         *
         * @param responseType The type of the response
         * @param projection   The projection, specifying which fields to retrieve
         * @param <T>          The type of the response
         * @return An object that can be used to retrieve the response
         */
        public <T> ProjectionSpecification<T> projectTo(Class<T> responseType, GraphQLResponseProjection projection) {
            return new ProjectionSpecification<>(request, projection, responseType);
        }
    }

    /**
     * Internal class used to retrieve the response.
     *
     * @param <T> The type of the response
     */
    @RequiredArgsConstructor
    public class ProjectionSpecification<T> {
        private final GraphQLOperationRequest request;
        private final GraphQLResponseProjection projection;
        private final Class<T> responseType;

        public Mono<ClientGraphQlResponse> execute() {
            GraphQLRequest graphQlRequest = new GraphQLRequest(request, projection);

            return graphQlClientSupplier.get()
                    .document(graphQlRequest.toQueryString())
                    .execute();
        }

        /**
         * Retrieve the response from the server.
         *
         * @return A Mono that will emit the response
         */
        public Mono<T> retrieve() {
            return execute()
                    .handle((response, sink) -> {
                        if (response.getErrors().isEmpty()) {
                            // map to the response type
                            extractResponseData(response).ifPresent(sink::next);
                        } else {
                            sink.error(toGraphQlException(response));
                        }
                    });
        }

        /**
         * Retrieve the response from the server as a list.
         *
         * @return A Mono that will emit the response
         */
        public Mono<List<T>> retrieveList() {
            return execute()
                    .handle((response, sink) -> {
                        if (response.getErrors().isEmpty()) {
                            sink.next(extractResponseDataList(response));
                        } else {
                            sink.error(toGraphQlException(response));
                        }
                    });
        }

        private Optional<T> extractResponseData(ClientGraphQlResponse response) {
            String retrievalName = request.getAlias() != null ? request.getAlias() : request.getOperationName();
            T result = response.field(retrievalName).toEntity(responseType);

            return Optional.ofNullable(result);
        }

        private List<T> extractResponseDataList(ClientGraphQlResponse response) {
            String retrievalName = request.getAlias() != null ? request.getAlias() : request.getOperationName();
            return response.field(retrievalName).toEntityList(responseType);
        }

        private static GraphqlErrorException toGraphQlException(ClientGraphQlResponse response) {
            // only handle the first error as only one exception can be thrown
            ResponseError error = response.getErrors().getFirst();
            return GraphqlErrorException.newErrorException()
                    .message(error.getMessage())
                    .path(error.getParsedPath())
                    .sourceLocations(error.getLocations())
                    .errorClassification(ErrorType.DataFetchingException)
                    .extensions(error.getExtensions())
                    .build();
        }

    }

}
