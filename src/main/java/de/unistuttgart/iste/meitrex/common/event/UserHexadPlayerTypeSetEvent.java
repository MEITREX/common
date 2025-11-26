package de.unistuttgart.iste.meitrex.common.event;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Map;
import java.util.UUID;

import jakarta.validation.constraints.NotNull;

/**
 * Event sent by the gamification service when a user's Hexad player type is set or updated.
 * Additionally send if the user's player type is requested for the first time
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserHexadPlayerTypeSetEvent {
    /**
     * The ID of the user whose Hexad player type was set.
     */
    @NotNull
    private UUID userId;
    
    /**
     * The primary Hexad player type that was set for the user.
     */
    @NotNull
    private HexadPlayerType primaryPlayerType;
    
    /**
     * Maps each HexadPlayerType to its score.
     */
    @NotNull
    private Map<HexadPlayerType, Double> playerTypePercentages;
}
