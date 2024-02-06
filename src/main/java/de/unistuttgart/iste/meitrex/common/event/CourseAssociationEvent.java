package de.unistuttgart.iste.meitrex.common.event;

import lombok.*;

import java.util.List;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CourseAssociationEvent {

    private UUID resourceId;
    private List<UUID> chapterIds;
    private CrudOperation operation;

}
