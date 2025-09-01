package de.unistuttgart.iste.meitrex.common.event;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class StageCompletedEvent {
    private UUID courseId;
    private UUID chapterId;
    private UUID sectionId;
    private UUID stageId;
    private UUID userId;
}
