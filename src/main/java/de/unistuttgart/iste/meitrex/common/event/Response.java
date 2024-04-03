package de.unistuttgart.iste.meitrex.common.event;

import java.util.UUID;
import lombok.*;
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Response {
    public UUID itemId;

    public float response;
}
