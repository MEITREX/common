package de.unistuttgart.iste.meitrex.common.user_handling;

import de.unistuttgart.iste.meitrex.common.exception.NoAccessToCourseException;
import lombok.NoArgsConstructor;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.UUID;
import java.util.stream.Stream;

/**
 * Static helper class which provides methods to validate that a user has access to a course.
 */
@NoArgsConstructor(access = lombok.AccessLevel.PRIVATE)
public class UserCourseAccessValidator {
    /**
     * Validates that a user has access to a course at the specified level (role).
     * If the user is a student, also takes into account start and end dates of the course and its published status.
     * If the user is a tutor or lecturer, the user always has access to the course as long as they are a member of it.
     * Throws a NoAccessToCourseException if the user does not have access to the course.
     *
     * @param user                The user to validate.
     * @param requiredMinimumRole The role which the user must at least have in this course for validation to succeed.
     * @param courseId            The id of the course to validate access to.
     * @throws NoAccessToCourseException If the user does not have access to the course.
     */
    public static void validateUserHasAccessToCourse(final LoggedInUser user,
                                                     final LoggedInUser.UserRoleInCourse requiredMinimumRole,
                                                     final UUID courseId) {
        if (GlobalPermissionAccessValidator.validateIsSuperUser(user)) {
            return;
        }
        final LoggedInUser.CourseMembership courseMembership = user.getCourseMemberships().stream()
                .filter(membership -> membership.getCourseId().equals(courseId))
                .findFirst()
                .orElseThrow(() -> new NoAccessToCourseException(courseId, "User is not a member of the course."));

        if (!courseMembership.getRole().hasAtLeastPermissionsOf(requiredMinimumRole)) {
            throw new NoAccessToCourseException(
                    courseId,
                    "User does not have the required role to access this data of the course."
            );
        }

        // if the user is a student, also check if the course is published and if the course has started and not ended
        if (!courseMembership.getRole().hasAtLeastPermissionsOf(LoggedInUser.UserRoleInCourse.TUTOR)) {
            if (!courseMembership.isPublished()) {
                throw new NoAccessToCourseException(courseId, "Course is not published.");
            }

            if (courseMembership.getStartDate().isAfter(OffsetDateTime.now())) {
                throw new NoAccessToCourseException(
                        courseId,
                        "User does not have access to the course because it has not started yet."
                );
            }

            if (courseMembership.getEndDate().isBefore(OffsetDateTime.now())) {
                throw new NoAccessToCourseException(
                        courseId,
                        "User does not have access to the course because it has ended."
                );
            }
        }
    }

    /**
     * Validates that a user has access to a list of courses at the specified level (role).
     *
     * @see #validateUserHasAccessToCourse(LoggedInUser, LoggedInUser.UserRoleInCourse, UUID)
     */
    public static void validateUserHasAccessToCourses(final LoggedInUser user,
                                                      final LoggedInUser.UserRoleInCourse requiredMinimumRole,
                                                      final List<UUID> courseIds) {
        for (final UUID courseId : courseIds) {
            validateUserHasAccessToCourse(user, requiredMinimumRole, courseId);
        }
    }

    /**
     * Validates that a user has access to a stream of courses at the specified level (role).
     *
     * @see #validateUserHasAccessToCourse(LoggedInUser, LoggedInUser.UserRoleInCourse, UUID)
     */
    public static void validateUserHasAccessToCourses(final LoggedInUser user,
                                                      final LoggedInUser.UserRoleInCourse requiredMinimumRole,
                                                      final Stream<UUID> courseIds) {
        courseIds.forEach(courseId -> validateUserHasAccessToCourse(user, requiredMinimumRole, courseId));
    }

}
