package de.unistuttgart.iste.meitrex.common.dapr;

import de.unistuttgart.iste.meitrex.common.event.*;
import de.unistuttgart.iste.meitrex.common.event.skilllevels.SkillEntityChangedEvent;
import de.unistuttgart.iste.meitrex.common.event.skilllevels.UserSkillLevelChangedEvent;
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
    public void notifyItemChanges(final UUID itemId, final CrudOperation operation){
        log.info("notifyItemChanges called with {} and {}", itemId,operation);
    }
    @Override
    public void notifyUserWorkedOnContent(final ContentProgressedEvent contentProgressedEvent) {
        log.info("notifyUserWorkedOnContent called with {}", contentProgressedEvent);
    }

    @Override
    public void notifyUserProgressUpdated(final UserProgressUpdatedEvent userProgressUpdatedEvent) {
        log.info("notifyUserProgressProcessed called with {}", userProgressUpdatedEvent);
    }

    /**
     * Method to notify when a file has been uploaded to MinIO for a media record.
     * @param mediaRecordFileCreatedEvent of the uploaded file
     */
    @Override
    public void notifyMediaRecordFileCreated(final MediaRecordFileCreatedEvent mediaRecordFileCreatedEvent) {
        log.info("notifyMediaRecordFileCreated called with {}", mediaRecordFileCreatedEvent);
    }

    /**
     * Method to notify when a media record has been deleted.
     * @param mediaRecordDeletedEvent of the deleted media record
     */
    @Override
    public void notifyMediaRecordDeleted(final MediaRecordDeletedEvent mediaRecordDeletedEvent) {
        log.info("notifyMediaRecordDeleted called with {}", mediaRecordDeletedEvent);
    }

    /**
     * Method to notify when media record links for a media content were set.
     * @param event of the content where links were set.
     */
    @Override
    public void notifyContentMediaRecordLinksSet(final ContentMediaRecordLinksSetEvent event) {
        log.info("notifyContentMediaRecordLinksSet called with {}", event);
    }

    @Override
    public void notifyAssessmentContentMutated(final AssessmentContentMutatedEvent event) {
        log.info("notifyAssessmentContentMutated called with {}", event);
    }

    @Override
    public void notifyForumActivity(final ForumActivityEvent forumActivityEvent) {
        log.info("notifyForumActivity called with {}", forumActivityEvent);
    }

    @Override
    public void notifyAchievementCompleted(final AchievementCompletedEvent achievementCompletedEvent) {
        log.info("notifyAchievementCompleted called with {}", achievementCompletedEvent);
    }

    @Override
    public void notifyUserSkillLevelChanged(final UserSkillLevelChangedEvent event) {
        log.info("notifyUserSkillLevelChanged called with {}", event);
    }

    @Override
    public void notifySkillEntityChanged(final SkillEntityChangedEvent event) {
        log.info("notifySkillEntityChanged called with {}", event);
    }

    @Override
    public void notificationEvent(final UUID courseId, final List<UUID> userIds, final ServerSource serverSource, final String link, final String title, final String message){
        log.info("notificationEvent called with {} and {} and {} and {} and {}", courseId, serverSource, link, title, message);
    }

    @Override
    public void notifyStageCompleted(final StageCompletedEvent event) {
        log.info("notifyStageCompleted called with {}", event);
    }
}
