package de.unistuttgart.iste.meitrex.common.exception;

import graphql.*;
import graphql.schema.DataFetchingEnvironment;
import jakarta.persistence.EntityNotFoundException;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.lang.NonNull;

import java.util.HashMap;
import java.util.Map;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
@Slf4j
public class ExceptionToGraphQlErrorConverter {

    /**
     * Resolves a throwable to a GraphQLError. The throwable is logged and the error type is determined.
     * More information on GraphQLErrors can be found
     * <a href="https://www.baeldung.com/spring-graphql-error-handling">here.</a>
     *
     * @param ex  the throwable
     * @param env the data fetching environment
     * @return the GraphQLError
     */
    public static GraphQLError resolveToSingleError(@NonNull final Throwable ex, @NonNull final DataFetchingEnvironment env) {
        log.error("Exception occurred during data fetching. Class: {}, Message: {}",
                ex.getClass().getSimpleName(), ex.getMessage());
        log.error("Exception trace: ", ex);

        return GraphqlErrorBuilder.newError()
                .extensions(buildExtensions(ex))
                .path(env.getExecutionStepInfo().getPath())
                .location(env.getField().getSourceLocation())
                .errorType(getErrorType(ex))
                .message(ex.getMessage())
                .build();
    }

    private static ErrorClassification getErrorType(final Throwable ex) {
        // special case for validation exceptions since we don't want to add the full graphql dependency
        // just for the exception type
        if (ex.getClass().getSimpleName().contains("ValidationException")) {
            return ErrorType.ValidationError;
        }

        return switch (ex) {
            case EntityNotFoundException ignored -> ErrorType.DataFetchingException;
            case GraphqlErrorException graphQLError -> graphQLError.getErrorType();
            case ExceptionWithGraphQlErrorType graphQLError -> graphQLError.getErrorType();
            default -> ErrorType.ExecutionAborted;
        };
    }

    /**
     * Adds the following information to the extensions map:
     * <ul>
     *     <li>exception: the simple name of the exception class</li>
     *     <li>message: the exception message</li>
     *     <li>thrownBy: the first element of the stack trace</li>
     * </ul>
     */
    private static Map<String, Object> buildExtensions(final Throwable ex) {
        final Map<String, Object> extensions = new HashMap<>();
        extensions.put("exception", ex.getClass().getSimpleName());
        extensions.put("message", ex.getMessage());
        // add basic stack trace information
        if (ex.getStackTrace().length > 0) {
            extensions.put("thrownBy", ex.getStackTrace()[0].toString());
        }

        if (ex instanceof GraphqlErrorException graphQLError) {
            extensions.putAll(graphQLError.getExtensions());
        }

        return extensions;
    }
}
