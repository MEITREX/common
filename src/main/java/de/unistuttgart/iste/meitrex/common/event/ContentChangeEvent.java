package de.unistuttgart.iste.meitrex.common.event;

import lombok.*;

import java.util.List;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ContentChangeEvent {

    private List<UUID> contentIds;

    private CrudOperation operation;
}
