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
public class ContentProgressedEvent {

    public enum ContentType {
        ASSIGNMENT, QUIZ, FLASHCARDS, OTHER
    }

    private UUID userId;
    private UUID contentId;
    private boolean success;
    private double correctness;
    private int hintsUsed;
    private Integer timeToComplete;
    private List<Response> responses;
    private MediaType mediaType;
    private ContentType contentType;

}
