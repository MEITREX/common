package de.unistuttgart.iste.meitrex.common.event;

import java.util.UUID;
import lombok.*;
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
/**
 * this class represents a users response to a given item on an assessment
 */
public class Response {
    public UUID itemId;

    public float response;
}
