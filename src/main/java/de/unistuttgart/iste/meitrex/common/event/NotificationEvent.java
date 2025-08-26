package de.unistuttgart.iste.meitrex.common.event;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class NotificationEvent {
    private UUID courseId;
    private List<UUID> userIds;
    private ServerSource serverSource;
    private String title;
    private String link;
    private String message;
    private OffsetDateTime timestamp;
}
