package de.unistuttgart.iste.gits.common.exception;

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

    public static GraphQLError resolveToSingleError(@NonNull Throwable ex, @NonNull DataFetchingEnvironment env) {
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

    private static ErrorType getErrorType(Throwable ex) {
        if (ex instanceof EntityNotFoundException) {
            return ErrorType.DataFetchingException;
        } else if (ex.getClass().getSimpleName().contains("ValidationException")) {
            return ErrorType.ValidationError;
        }
        // HINT add more error types here
        return ErrorType.ExecutionAborted;
    }

    private static Map<String, Object> buildExtensions(Throwable ex) {
        Map<String, Object> extensions = new HashMap<>();
        extensions.put("exception", ex.getClass().getSimpleName());
        extensions.put("message", ex.getMessage());
        if (ex.getStackTrace().length > 0) {
            extensions.put("thrownBy", ex.getStackTrace()[0].toString());
        }

        return extensions;
    }
}
