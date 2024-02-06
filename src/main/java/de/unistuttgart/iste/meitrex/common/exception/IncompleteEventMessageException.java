package de.unistuttgart.iste.meitrex.common.exception;

public class IncompleteEventMessageException extends Exception {

    public static final String ERROR_INCOMPLETE_MESSAGE = "incomplete message received: all fields of a message must not be non-null!";

    public IncompleteEventMessageException(final String errorMessage) {
        super(errorMessage);
    }
}
