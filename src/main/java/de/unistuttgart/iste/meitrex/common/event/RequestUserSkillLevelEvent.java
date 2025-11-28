package de.unistuttgart.iste.meitrex.common.event;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

import jakarta.validation.constraints.NotNull;

/**
 * Event to request the skill levels for a specific user.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RequestUserSkillLevelEvent {
    /**
     * The ID of the user whose skill levels are being requested.
     */
    @NotNull
    private UUID userId;
}
