package de.unistuttgart.iste.meitrex.common.event;

import java.util.List;
import java.util.UUID;
import lombok.*;
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ItemResponse {
    public UUID itemId;

    public float response;

    public List<UUID>skillIds;

    public List<LevelOfBloomsTaxonomy> levelsOfBloomsTaxonomy;
}
