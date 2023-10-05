package de.unistuttgart.iste.gits.common.user_handling;

import de.unistuttgart.iste.gits.common.TestUserUtil;
import de.unistuttgart.iste.gits.common.exception.NoAccessToCourseException;
import org.junit.jupiter.api.Test;

import java.time.OffsetDateTime;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;

class UserCourseAccessValidatorTest {
    @Test
    void testValidateUserHasAccessToCourse() {

        LoggedInUser.CourseMembership membership = TestUserUtil.courseMembershipBuilder()
                .role(LoggedInUser.UserRoleInCourse.STUDENT)
                .build();

        LoggedInUser user = TestUserUtil.createUserWithMemberships(membership);

        assertDoesNotThrow(() -> UserCourseAccessValidator.validateUserHasAccessToCourse(
                user,
                LoggedInUser.UserRoleInCourse.STUDENT,
                user.getCourseMemberships().get(0).getCourseId()
        ));
    }

    @Test
    void testValidateUserHasNoAccessToCourseNotMemberOf() {

        UUID randomCourseID = UUID.randomUUID();

        LoggedInUser.CourseMembership membership = TestUserUtil.courseMembershipBuilder()
                .role(LoggedInUser.UserRoleInCourse.STUDENT)
                .build();

        LoggedInUser user = TestUserUtil.createUserWithMemberships(membership);



        assertThrows(NoAccessToCourseException.class, () -> UserCourseAccessValidator.validateUserHasAccessToCourse(
                user,
                LoggedInUser.UserRoleInCourse.STUDENT,
                randomCourseID
        ));
    }

    @Test
    void testValidateUserHasNoAccessToCourseNotPublished() {

        LoggedInUser.CourseMembership membership = TestUserUtil.courseMembershipBuilder()
                .published(false)
                .role(LoggedInUser.UserRoleInCourse.STUDENT)
                .build();

        LoggedInUser user = TestUserUtil.createUserWithMemberships(membership);

        UUID userCourseID = user.getCourseMemberships().get(0).getCourseId();

        assertThrows(NoAccessToCourseException.class, () -> UserCourseAccessValidator.validateUserHasAccessToCourse(
                user,
                LoggedInUser.UserRoleInCourse.STUDENT,
                userCourseID
        ));
    }

    @Test
    void testValidateTutorHasAccessToCourseNotPublished() {

        LoggedInUser.CourseMembership membership = TestUserUtil.courseMembershipBuilder()
                .published(false)
                .role(LoggedInUser.UserRoleInCourse.TUTOR)
                .build();

        LoggedInUser user = TestUserUtil.createUserWithMemberships(membership);

        assertDoesNotThrow(() -> UserCourseAccessValidator.validateUserHasAccessToCourse(
                user,
                LoggedInUser.UserRoleInCourse.TUTOR,
                user.getCourseMemberships().get(0).getCourseId()
        ));
    }

    @Test
    void testValidateUserHasNoAccessToCourseWrongRole() {

        LoggedInUser.CourseMembership membership = TestUserUtil.courseMembershipBuilder()
                .role(LoggedInUser.UserRoleInCourse.STUDENT)
                .build();

        LoggedInUser user = TestUserUtil.createUserWithMemberships(membership);

        UUID userCourseID = user.getCourseMemberships().get(0).getCourseId();

        assertThrows(NoAccessToCourseException.class, () -> UserCourseAccessValidator.validateUserHasAccessToCourse(
                user,
                LoggedInUser.UserRoleInCourse.TUTOR,
                userCourseID
        ));
    }

    @Test
    void testValidateUserHasNoAccessToCourseNotStartedYet() {

        LoggedInUser.CourseMembership membership = TestUserUtil.courseMembershipBuilder()
                .startDate(OffsetDateTime.now().plusDays(1))
                .endDate(OffsetDateTime.now().plusDays(2))
                .role(LoggedInUser.UserRoleInCourse.STUDENT)
                .build();

        LoggedInUser user = TestUserUtil.createUserWithMemberships(membership);

        UUID userCourseID = user.getCourseMemberships().get(0).getCourseId();

        assertThrows(NoAccessToCourseException.class, () -> UserCourseAccessValidator.validateUserHasAccessToCourse(
                user,
                LoggedInUser.UserRoleInCourse.STUDENT,
                userCourseID
        ));
    }

    @Test
    void testValidateTutorHasAccessToCourseNotStartedYet() {

        LoggedInUser.CourseMembership membership = TestUserUtil.courseMembershipBuilder()
                .startDate(OffsetDateTime.now().plusDays(1))
                .endDate(OffsetDateTime.now().plusDays(2))
                .role(LoggedInUser.UserRoleInCourse.TUTOR)
                .build();

        LoggedInUser user = TestUserUtil.createUserWithMemberships(membership);

        assertDoesNotThrow(() -> UserCourseAccessValidator.validateUserHasAccessToCourse(
                user,
                LoggedInUser.UserRoleInCourse.TUTOR,
                user.getCourseMemberships().get(0).getCourseId()
        ));
    }

    @Test
    void testValidateUserHasNoAccessToCourseEnded() {

        LoggedInUser.CourseMembership membership = TestUserUtil.courseMembershipBuilder()
                .startDate(OffsetDateTime.now().minusDays(2))
                .endDate(OffsetDateTime.now().minusDays(1))
                .role(LoggedInUser.UserRoleInCourse.STUDENT)
                .build();

        LoggedInUser user = TestUserUtil.createUserWithMemberships(membership);

        UUID userCourseID = user.getCourseMemberships().get(0).getCourseId();

        assertThrows(NoAccessToCourseException.class, () -> UserCourseAccessValidator.validateUserHasAccessToCourse(
                user,
                LoggedInUser.UserRoleInCourse.STUDENT,
                userCourseID
        ));
    }

    @Test
    void testValidateTutorHasAccessToCourseEnded() {

        LoggedInUser.CourseMembership membership = TestUserUtil.courseMembershipBuilder()
                .startDate(OffsetDateTime.now().minusDays(2))
                .endDate(OffsetDateTime.now().minusDays(1))
                .role(LoggedInUser.UserRoleInCourse.TUTOR)
                .build();

        LoggedInUser user = TestUserUtil.createUserWithMemberships(membership);

        assertDoesNotThrow(() -> UserCourseAccessValidator.validateUserHasAccessToCourse(
                user,
                LoggedInUser.UserRoleInCourse.TUTOR,
                user.getCourseMemberships().get(0).getCourseId()
        ));
    }
}
