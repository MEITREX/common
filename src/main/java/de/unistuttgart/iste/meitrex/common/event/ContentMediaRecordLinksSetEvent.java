package de.unistuttgart.iste.meitrex.common.event;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.util.List;
import java.util.UUID;

@Data
@AllArgsConstructor
@Builder
public class ContentMediaRecordLinksSetEvent {
    private UUID contentId;
    private List<UUID> mediaRecordIds;
}
