package de.unistuttgart.iste.meitrex.common.event;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MediaRecordWorkedOnEvent {
    private UUID userId;
    private UUID mediaRecordId;
    private boolean wasAlreadyWorkedOn;
}
