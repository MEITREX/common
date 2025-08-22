package de.unistuttgart.iste.meitrex.common.event;

import lombok.*;

import java.util.List;
import java.util.UUID;

/**
 * This class represents a user progress log event.
 */
@Data
@Builder
@AllArgsConstructor
public class UserProgressUpdatedEvent {

    private Long sequenceNo;
    private UUID userId;
    private UUID contentId;
    private UUID chapterId;
    private UUID courseId;
    private int attempt;
    private boolean success;
    private double correctness;
    private int hintsUsed;
    private Integer timeToComplete;
    private List<ItemResponse> responses;
    private MediaType mediaType;


}
