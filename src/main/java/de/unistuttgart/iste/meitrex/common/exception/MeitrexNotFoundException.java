package de.unistuttgart.iste.meitrex.common.exception;

import graphql.ErrorType;
import jakarta.persistence.EntityNotFoundException;

/**
 * Exception that is thrown when a resource is not found.
 */
public class MeitrexNotFoundException extends EntityNotFoundException implements ExceptionWithGraphQlErrorType {
    public MeitrexNotFoundException(String entityName, Object id) {
        this("Resource " + entityName + " with id " + id + " not found");
    }

    public MeitrexNotFoundException(String reason) {
        super(reason);
    }


    @Override
    public ErrorType getErrorType() {
        return ErrorType.DataFetchingException;
    }
}
