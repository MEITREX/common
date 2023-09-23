package de.unistuttgart.iste.gits.common.user_handling;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.UUID;

/**
 * This class represents user data for a logged-in user as provided by keycloak.
 */
@Getter
public class LoggedInUser {
    private final UUID id;
    private final String userName;
    private final String firstName;
    private final String lastName;
    private final List<CourseMembership> courseMemberships;

    public LoggedInUser(@JsonProperty("id") final UUID id,
                        @JsonProperty("userName") final String userName,
                        @JsonProperty("firstName") final String firstName,
                        @JsonProperty("lastName") final String lastName,
                        @JsonProperty("courseMemberships") final List<CourseMembership> courseMemberships) {
        this.id = id;
        this.userName = userName;
        this.firstName = firstName;
        this.lastName = lastName;
        this.courseMemberships = courseMemberships;
    }

    @Getter
    public static class CourseMembership {
        private final UUID courseId;
        private final UserRoleInCourse role;
        private final boolean published;
        private final OffsetDateTime startDate;
        private final OffsetDateTime endDate;

        public CourseMembership(final UUID courseId,
                                final UserRoleInCourse role,
                                final boolean published,
                                final OffsetDateTime startDate,
                                final OffsetDateTime endDate) {
            this.courseId = courseId;
            this.role = role;
            this.published = published;
            this.startDate = startDate;
            this.endDate = endDate;
        }

        /**
         * Constructor used for JSON deserialization.
         */
        public CourseMembership(@JsonProperty("courseId") final UUID courseId,
                                @JsonProperty("role") final UserRoleInCourse role,
                                @JsonProperty("published") final boolean published,
                                @JsonProperty("startDate") final String startDate,
                                @JsonProperty("endDate") final String endDate) {
            this.courseId = courseId;
            this.role = role;
            this.published = published;
            this.startDate = OffsetDateTime.parse(startDate);
            this.endDate = OffsetDateTime.parse(endDate);
        }
    }

    public enum UserRoleInCourse {
        STUDENT,
        TUTOR,
        ADMINISTRATOR
    }
}
