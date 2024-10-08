package de.unistuttgart.iste.meitrex.common.event;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.annotation.Nullable;
import java.util.List;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AssessmentContentMutatedEvent {
    private UUID courseId;
    private UUID assessmentId;
    private AssessmentType assessmentType;
    private List<String> textualRepresentation;
}
