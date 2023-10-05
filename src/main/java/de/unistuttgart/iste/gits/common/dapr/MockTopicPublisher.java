package de.unistuttgart.iste.gits.common.dapr;

import de.unistuttgart.iste.gits.common.event.*;
import lombok.extern.slf4j.Slf4j;

import java.util.List;
import java.util.UUID;

/**
 * MockTopicPublisher for local development.
 * It will log all messages instead of sending them to the dapr topic.
 *
 */
@Slf4j
public class MockTopicPublisher extends TopicPublisher {

    public MockTopicPublisher() {
        super(null);
    }

    @Override
    public void notifyCourseChanges(final UUID courseId, final CrudOperation operation){
        log.info("notifyCourseChanges called with {} and {}", courseId, operation);
    }

    @Override
    public void notifyChapterChanges(final List<UUID> chapterIds, final CrudOperation operation) {
        log.info("notifyChapterChanges called with {} and {}", chapterIds, operation);
    }

    @Override
    public void notifyContentChanges(final List<UUID> contentEntityIds, final CrudOperation operation) {
        log.info("notifyContentChanges called with {} and {}", contentEntityIds, operation);
    }

    @Override
    public void notifyUserWorkedOnContent(final ContentProgressedEvent contentProgressedEvent) {
        log.info("notifyUserWorkedOnContent called with {}", contentProgressedEvent);
    }

    @Override
    public void notifyUserProgressUpdated(final UserProgressUpdatedEvent userProgressUpdatedEvent) {
        log.info("notifyUserProgressProcessed called with {}", userProgressUpdatedEvent);
    }
}
