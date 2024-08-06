package de.unistuttgart.iste.meitrex.common.event;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.util.UUID;

@Data
@AllArgsConstructor
@Builder
public class MediaRecordDeletedEvent {
    private UUID mediaRecordId;
}
