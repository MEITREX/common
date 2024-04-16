package de.unistuttgart.iste.meitrex.common.exception;

import graphql.ErrorType;

/**
 * Exception to be thrown when a resource already exists, but it is tried to be created again.
 */
public class ResourceAlreadyExistsException extends RuntimeException implements ExceptionWithGraphQlErrorType {
    public ResourceAlreadyExistsException(String reason) {
        super(reason);
    }

    public ResourceAlreadyExistsException(String resourceName, Object id) {
        this(resourceName + " with id " + id + " already exists.");
    }

    public ResourceAlreadyExistsException(Class<?> resourceClass, Object id) {
        this(resourceClass.getSimpleName() + " with id " + id + " already exists.");
    }

    @Override
    public ErrorType getErrorType() {
        return ErrorType.ValidationError;
    }
}
