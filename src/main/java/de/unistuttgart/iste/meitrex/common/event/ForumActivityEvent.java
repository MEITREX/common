package de.unistuttgart.iste.meitrex.common.event;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.util.UUID;

@Data
@Builder
@AllArgsConstructor
public class ForumActivityEvent {
    private UUID userId;
    private UUID forumId;
    private UUID courseId;
    private ForumActivity activity;
}
