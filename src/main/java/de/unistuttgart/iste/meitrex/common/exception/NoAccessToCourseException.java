package de.unistuttgart.iste.meitrex.common.exception;

import java.util.UUID;

/**
 * Exception thrown when a user does not have access to a course.
 */
public class NoAccessToCourseException extends RuntimeException {
    public NoAccessToCourseException(final UUID courseId) {
        super("User has no access to course with id " + courseId + ".");
    }

    public NoAccessToCourseException(final UUID courseId, final String noAccessReason) {
        super("User has no access to course with id " + courseId + ". Reason: " + noAccessReason);
    }
}
