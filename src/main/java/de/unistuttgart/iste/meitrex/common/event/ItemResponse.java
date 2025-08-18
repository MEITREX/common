package de.unistuttgart.iste.meitrex.common.event;

import java.util.List;
import java.util.UUID;

import de.unistuttgart.iste.meitrex.generated.dto.BloomLevel;
import lombok.*;

/**
 * this class represents a users response together with information of the answered questions
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ItemResponse {
    public UUID itemId;

    public float response;

    public List<UUID>skillIds;

    public List<BloomLevel> levelsOfBloomsTaxonomy;
}
