package de.unistuttgart.iste.gits.common.user_handling;

import de.unistuttgart.iste.gits.common.exception.NoAccessToCourseException;
import org.junit.jupiter.api.Test;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class UserCourseAccessValidatorTest {
    @Test
    void testValidateUserHasAccessToCourse() {
        LoggedInUser user = new LoggedInUser(
                UUID.randomUUID(),
                "TestUser",
                "Test",
                "User",
                List.of(
                        new LoggedInUser.CourseMembership(
                                UUID.randomUUID(),
                                LoggedInUser.UserRoleInCourse.STUDENT,
                                true,
                                OffsetDateTime.now().minusDays(1),
                                OffsetDateTime.now().plusDays(1)
                        )
                )
        );

        assertDoesNotThrow(() -> UserCourseAccessValidator.validateUserHasAccessToCourse(
                user,
                LoggedInUser.UserRoleInCourse.STUDENT,
                user.getCourseMemberships().get(0).getCourseId()
        ));
    }

    @Test
    void testValidateUserHasNoAccessToCourseNotMemberOf() {
        LoggedInUser user = new LoggedInUser(
                UUID.randomUUID(),
                "TestUser",
                "Test",
                "User",
                List.of(
                        new LoggedInUser.CourseMembership(
                                UUID.randomUUID(),
                                LoggedInUser.UserRoleInCourse.STUDENT,
                                true,
                                OffsetDateTime.now().minusDays(1),
                                OffsetDateTime.now().plusDays(1)
                        )
                )
        );

        assertThrows(NoAccessToCourseException.class, () -> UserCourseAccessValidator.validateUserHasAccessToCourse(
                user,
                LoggedInUser.UserRoleInCourse.STUDENT,
                UUID.randomUUID()
        ));
    }

    @Test
    void testValidateUserHasNoAccessToCourseNotPublished() {
        LoggedInUser user = new LoggedInUser(
                UUID.randomUUID(),
                "TestUser",
                "Test",
                "User",
                List.of(
                        new LoggedInUser.CourseMembership(
                                UUID.randomUUID(),
                                LoggedInUser.UserRoleInCourse.STUDENT,
                                false,
                                OffsetDateTime.now().minusDays(1),
                                OffsetDateTime.now().plusDays(1)
                        )
                )
        );

        assertThrows(NoAccessToCourseException.class, () -> UserCourseAccessValidator.validateUserHasAccessToCourse(
                user,
                LoggedInUser.UserRoleInCourse.STUDENT,
                user.getCourseMemberships().get(0).getCourseId()
        ));
    }

    @Test
    void testValidateUserHasNoAccessToCourseWrongRole() {
        LoggedInUser user = new LoggedInUser(
                UUID.randomUUID(),
                "TestUser",
                "Test",
                "User",
                List.of(
                        new LoggedInUser.CourseMembership(
                                UUID.randomUUID(),
                                LoggedInUser.UserRoleInCourse.STUDENT,
                                true,
                                OffsetDateTime.now().minusDays(1),
                                OffsetDateTime.now().plusDays(1)
                        )
                )
        );

        assertThrows(NoAccessToCourseException.class, () -> UserCourseAccessValidator.validateUserHasAccessToCourse(
                user,
                LoggedInUser.UserRoleInCourse.TUTOR,
                user.getCourseMemberships().get(0).getCourseId()
        ));
    }

    @Test
    void testValidateUserHasNoAccessToCourseNotStartedYet() {
        LoggedInUser user = new LoggedInUser(
                UUID.randomUUID(),
                "TestUser",
                "Test",
                "User",
                List.of(
                        new LoggedInUser.CourseMembership(
                                UUID.randomUUID(),
                                LoggedInUser.UserRoleInCourse.STUDENT,
                                true,
                                OffsetDateTime.now().plusDays(1),
                                OffsetDateTime.now().plusDays(2)
                        )
                )
        );

        assertThrows(NoAccessToCourseException.class, () -> UserCourseAccessValidator.validateUserHasAccessToCourse(
                user,
                LoggedInUser.UserRoleInCourse.STUDENT,
                user.getCourseMemberships().get(0).getCourseId()
        ));
    }

    @Test
    void testValidateUserHasNoAccessToCourseEnded() {
        LoggedInUser user = new LoggedInUser(
                UUID.randomUUID(),
                "TestUser",
                "Test",
                "User",
                List.of(
                        new LoggedInUser.CourseMembership(
                                UUID.randomUUID(),
                                LoggedInUser.UserRoleInCourse.STUDENT,
                                true,
                                OffsetDateTime.now().minusDays(2),
                                OffsetDateTime.now().minusDays(1)
                        )
                )
        );

        assertThrows(NoAccessToCourseException.class, () -> UserCourseAccessValidator.validateUserHasAccessToCourse(
                user,
                LoggedInUser.UserRoleInCourse.STUDENT,
                user.getCourseMemberships().get(0).getCourseId()
        ));
    }
}
