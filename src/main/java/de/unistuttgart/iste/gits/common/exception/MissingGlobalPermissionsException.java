package de.unistuttgart.iste.gits.common.exception;

/**
 * Exception thrown when a user does not have the required global Role.
 */
public class MissingGlobalPermissionsException extends RuntimeException{
    public MissingGlobalPermissionsException() {
        super("User is missing the required permissions.");
    }

    public MissingGlobalPermissionsException(final String noAccessReason) {
        super("User is missing the required permission. Reason: " + noAccessReason);
    }
}
