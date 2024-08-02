package de.unistuttgart.iste.meitrex.common.event;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

/**
 * Event which is raised when a file for the specified media record is uploaded to MinIO.
 */
@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class MediaRecordFileCreatedEvent {
    private UUID mediaRecordId;
}
