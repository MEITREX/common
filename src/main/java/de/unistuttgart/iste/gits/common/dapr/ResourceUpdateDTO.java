package de.unistuttgart.iste.gits.common.dapr;

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
public class ResourceUpdateDTO {

    UUID entityId;
    List<UUID> contentIds;
    CrudOperation operation;
}
