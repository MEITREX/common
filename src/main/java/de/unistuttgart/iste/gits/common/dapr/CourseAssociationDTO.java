package de.unistuttgart.iste.gits.common.dapr;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CourseAssociationDTO {

    private UUID resourceId;
    private UUID chapterId;
    private CrudOperation operation;

}
