package de.unistuttgart.iste.meitrex.common.event;

import de.unistuttgart.iste.meitrex.generated.dto.UserRoleInCourse;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.annotation.Nullable;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserCourseMembershipChangedEvent {
    @NotNull
    private UUID userId;
    @NotNull
    private UUID courseId;
    /**
     * The previous role of the user in the course, or null if the user was not a member of the course before.
     */
    @Nullable
    private UserRoleInCourse previousRole;
    /**
     * The new role of the user in the course, or null if the user is no longer a member of the course.
     */
    @Nullable
    private UserRoleInCourse newRole;
}
