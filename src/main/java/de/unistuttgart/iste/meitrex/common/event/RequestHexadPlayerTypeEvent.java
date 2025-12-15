package de.unistuttgart.iste.meitrex.common.event;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

import jakarta.validation.constraints.NotNull;

/**
 * Event to request the Hexad player type for a specific user.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RequestHexadPlayerTypeEvent {
    /**
     * The ID of the user whose Hexad player type is being requested.
     */
    @NotNull
    private UUID userId;
}
