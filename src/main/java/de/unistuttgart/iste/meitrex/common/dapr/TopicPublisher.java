package de.unistuttgart.iste.meitrex.common.dapr;

import de.unistuttgart.iste.meitrex.common.event.*;
import io.dapr.client.DaprClient;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.cache.spi.support.AbstractReadWriteAccess;

import java.util.List;
import java.util.UUID;

/**
 * The TopicPublisher for all services.
 * Contains a generic publishEvent method to publish the events in a unified way,
 * as well as methods for the various events that can be published.
 */
@Slf4j
@RequiredArgsConstructor
public class TopicPublisher {

    private static final String PUBSUB_NAME = "meitrex";

    private final DaprClient client;

    /**
     * Method used to publish dapr messages to a daprTopic
     *
     * @param dto message
     */
    protected void publishEvent(final Object dto, final DaprTopic daprTopic) {
        client.publishEvent(PUBSUB_NAME, daprTopic.getTopic(), dto)
                .doOnSuccess(response -> log.debug("Published message to daprTopic {}: {}", daprTopic.getTopic(), response))
                .doOnError(error -> log.error("Error while publishing message to daprTopic {}: {}", daprTopic.getTopic(), error.getMessage()))
                .subscribe();
    }

    /**
     * Method to notify when there are course changes
     *
     * @param courseId UUID of the course that changed
     * @param operation that was performed {@link CrudOperation}
     */
    public void notifyCourseChanges(final UUID courseId, final CrudOperation operation) {
        final CourseChangeEvent dto = CourseChangeEvent.builder()
                .courseId(courseId)
                .operation(operation)
                .build();
        publishEvent(dto, DaprTopic.COURSE_CHANGED);
    }

    /**
     * Method to notify when there are chapter changes
     *
     * @param chapterIds of the chapters that changed
     * @param operation that was performed {@link CrudOperation}
     */
    public void notifyChapterChanges(final List<UUID> chapterIds, final CrudOperation operation) {
        final ChapterChangeEvent dto = ChapterChangeEvent.builder()
                .chapterIds(chapterIds)
                .operation(operation)
                .build();
        publishEvent(dto, DaprTopic.CHAPTER_CHANGED);
    }

    /**
     * Method to notify when there are changes to content
     * @param contentEntityIds of the Content that has changed
     * @param operation that was performed {@link CrudOperation}
     */
    public void notifyContentChanges(final List<UUID> contentEntityIds, final CrudOperation operation) {
        final ContentChangeEvent dto = ContentChangeEvent.builder()
                .contentIds(contentEntityIds)
                .operation(operation)
                .build();

        publishEvent(dto, DaprTopic.CONTENT_CHANGED);
    }

    /**
     * Method to notify when there are changes to an item
     * @param itemId of the item that has changed
     * @param operation that was performed {@link CrudOperation}
     */
    public void notifyItemChanges(final UUID itemId, final CrudOperation operation){
        final ItemChangeEvent dto = ItemChangeEvent.builder()
                .itemId(itemId)
                .operation(operation)
                .build();

        publishEvent(dto, DaprTopic.ITEM_CHANGED);
    }

    /**
     * Method to notify when a user has worked on a content.
     *
     * @param contentProgressedEvent of the worked on content
     */
    public void notifyUserWorkedOnContent(final ContentProgressedEvent contentProgressedEvent) {
        publishEvent(contentProgressedEvent, DaprTopic.CONTENT_PROGRESSED);
    }

    /**
     * Method to notify when the content service has processed the completion of a content worked on by a user.
     * @param userProgressUpdatedEvent of the processed content
     */
    public void notifyUserProgressUpdated(final UserProgressUpdatedEvent userProgressUpdatedEvent) {
        publishEvent(userProgressUpdatedEvent, DaprTopic.USER_PROGRESS_UPDATED);
    }

}
