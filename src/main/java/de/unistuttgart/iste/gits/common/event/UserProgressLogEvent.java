package de.unistuttgart.iste.gits.common.event;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.util.UUID;

/**
 * This class represents a user progress log event.
 */
@Data
@Builder
@AllArgsConstructor
public class UserProgressLogEvent {

    private UUID userId;
    private UUID contentId;
    private boolean success;
    private double correctness;
    private int hintsUsed;
    private Integer timeToComplete;

}
