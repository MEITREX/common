package de.unistuttgart.iste.meitrex.common.event;

import lombok.Data;

import java.util.UUID;

@Data
public class MediaRecordDeletedEvent {
    private UUID mediaRecordId;
}
