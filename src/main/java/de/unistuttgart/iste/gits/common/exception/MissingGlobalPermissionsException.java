package de.unistuttgart.iste.gits.common.exception;

public class MissingGlobalPermissionsException extends RuntimeException{
    public MissingGlobalPermissionsException() {
        super("User is missing the required permissions.");
    }

    public MissingGlobalPermissionsException(final String noAccessReason) {
        super("User is missing the required permission. Reason: " + noAccessReason);
    }
}
