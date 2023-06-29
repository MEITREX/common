package de.unistuttgart.iste.gits.common.event;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

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
