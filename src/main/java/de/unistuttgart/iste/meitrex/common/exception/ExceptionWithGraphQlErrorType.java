package de.unistuttgart.iste.meitrex.common.exception;

import graphql.ErrorType;

/**
 * Interface for exceptions that have a {@link ErrorType}.
 */
public interface ExceptionWithGraphQlErrorType {

    ErrorType getErrorType();
}
