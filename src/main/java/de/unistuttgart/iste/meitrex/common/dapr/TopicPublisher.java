package de.unistuttgart.iste.meitrex.common.dapr;

import de.unistuttgart.iste.meitrex.common.event.*;
import de.unistuttgart.iste.meitrex.common.event.skilllevels.SkillEntityChangedEvent;
import de.unistuttgart.iste.meitrex.common.event.skilllevels.UserSkillLevelChangedEvent;
import io.dapr.client.DaprClient;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.time.OffsetDateTime;
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

    /**
     * Method to notify when a file has been uploaded to MinIO for a media record.
     * @param mediaRecordFileCreatedEvent of the uploaded file
     */
    public void notifyMediaRecordFileCreated(final MediaRecordFileCreatedEvent mediaRecordFileCreatedEvent) {
        publishEvent(mediaRecordFileCreatedEvent, DaprTopic.MEDIA_RECORD_FILE_CREATED);
    }

    /**
     * Method to notify when a media record has been deleted.
     * @param mediaRecordDeletedEvent of the deleted media record
     */
    public void notifyMediaRecordDeleted(final MediaRecordDeletedEvent mediaRecordDeletedEvent) {
        publishEvent(mediaRecordDeletedEvent, DaprTopic.MEDIA_RECORD_DELETED);
    }

    /**
     * Method to notify when media record links for a media content were set.
     * @param event of the content where links were set.
     */
    public void notifyContentMediaRecordLinksSet(final ContentMediaRecordLinksSetEvent event) {
        publishEvent(event, DaprTopic.CONTENT_MEDIA_RECORD_LINKS_SET);
    }

    public void notifyAssessmentContentMutated(final AssessmentContentMutatedEvent event) {
        publishEvent(event, DaprTopic.ASSESSMENT_CONTENT_MUTATED);
    }

    /**
     * Method to notify when a user posted something in the forum
     * @param event of the forum and the activity the user did in the forum
     */
    public void notifyForumActivity(final ForumActivityEvent event) {
        publishEvent(event, DaprTopic.FORUM_ACTIVITY);
    }

    /**
     * Method to notify when a user completed an achievement
     * @param event of the achievement that was completed and the user that completed the achievement
     */
    public void notifyAchievementCompleted(final AchievementCompletedEvent event) {
        publishEvent(event, DaprTopic.FORUM_ACTIVITY);
    }

    public void notifyUserSkillLevelChanged(final UserSkillLevelChangedEvent event) {
        publishEvent(event, DaprTopic.USER_SKILL_LEVEL_CHANGED);
    }

    public void notifySkillEntityChanged(final SkillEntityChangedEvent event) {
        publishEvent(event, DaprTopic.SKILL_ENTITY_CHANGED);
    }

    public void notifyStageCompleted(final StageCompletedEvent event) {
        publishEvent(event, DaprTopic.STAGE_COMPLETED);
    }

    /**
     * Method to notify when a user the AI Tutor a question
     * @param event of the question that was asked, the corresponding category, the user that asked it and the courseId
     */
    public void notifyTutorQuestionAsked(final AskedTutorAQuestionEvent event) {
        publishEvent(event, DaprTopic.ASKED_TUTOR_A_QUESTION);
    }

    /**
     * Method to notify when a notification is build and should be sent to Notification Service
     * @param courseId of changed course
     * @param serverSource of the source service
     * @param link of link
     * @param message of notification message
     */
    public void notificationEvent(final UUID courseId, final List<UUID> userIds, final ServerSource serverSource, final String link, final String Title, final String message) {
        final NotificationEvent dto = NotificationEvent.builder()
                .courseId(courseId)
                .userIds(userIds)
                .serverSource(serverSource)
                .link(link)
                .title(Title)
                .message(message)
                .timestamp(OffsetDateTime.now())
                .build();
        publishEvent(dto, DaprTopic.NOTIFICATION_EVENT);
    }
}
