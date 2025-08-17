package de.unistuttgart.iste.meitrex.common.event.skilllevels;

import de.unistuttgart.iste.meitrex.generated.dto.BloomLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserSkillLevelChangedEvent {
    /**
     * ID of the user whose skill level has changed.
     */
    private UUID userId;

    /**
     * ID of the skill whose level has changed.
     */
    private UUID skillId;

    /**
     * The bloom level of which the value was changed.
     */
    private BloomLevel bloomLevel;

    /**
     * The new skill level of the user for the specified skill. Range 0..1
     */
    private float newValue;

    /**
     * The previous skill level of the user for the specified skill. Range 0..1
     */
    private float oldValue;
}
