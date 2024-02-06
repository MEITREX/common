package de.unistuttgart.iste.meitrex.common.event;

import lombok.*;

import java.util.List;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ResourceUpdateEvent {

    private UUID entityId;
    private List<UUID> contentIds;
    private CrudOperation operation;
}
