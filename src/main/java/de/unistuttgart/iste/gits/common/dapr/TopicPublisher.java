package de.unistuttgart.iste.gits.common.dapr;

import de.unistuttgart.iste.gits.common.event.*;
import io.dapr.client.DaprClient;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

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

    private static final String PUBSUB_NAME = "gits";

    private final DaprClient client;

    /**
     * Method used to publish dapr messages to a topic
     *
     * @param dto message
     */
    protected void publishEvent(final Object dto, final Topic topic) {
        client.publishEvent(PUBSUB_NAME, topic.getTopic(), dto)
                .doOnSuccess(response -> log.debug("Published message to topic {}: {}", topic.getTopic(), response))
                .doOnError(error -> log.error("Error while publishing message to topic {}: {}", topic.getTopic(), error.getMessage()))
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
        publishEvent(dto, Topic.COURSE_CHANGED);
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
        publishEvent(dto, Topic.CHAPTER_CHANGED);
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

        publishEvent(dto, Topic.CONTENT_CHANGED);
    }

    /**
     * Method to notify when a user has worked on a content.
     *
     * @param userProgressLogEvent of the worked on content
     */
    public void notifyUserWorkedOnContent(final UserProgressLogEvent userProgressLogEvent) {
        publishEvent(userProgressLogEvent, Topic.CONTENT_PROGRESSED);
    }

    /**
     * Method to notify when the content service has processed the completion of a content worked on by a user.
     * @param userProgressLogEvent of the processed content
     */
    public void notifyUserProgressProcessed(final UserProgressLogEvent userProgressLogEvent) {
        publishEvent(userProgressLogEvent, Topic.USER_PROGRESS_UPDATED);
    }

}
