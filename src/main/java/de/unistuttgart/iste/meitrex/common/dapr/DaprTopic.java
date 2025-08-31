package de.unistuttgart.iste.meitrex.common.dapr;

public enum DaprTopic {

    COURSE_CHANGED("course-changed"),
    CHAPTER_CHANGED("chapter-changed"),

    CONTENT_PROGRESSED("content-progressed"),

    CONTENT_CHANGED("content-changed"),

    USER_PROGRESS_UPDATED("user-progress-updated"),

    MEDIA_RECORD_FILE_CREATED("media-record-file-created"),

    MEDIA_RECORD_DELETED("media-record-deleted"),

    CONTENT_MEDIA_RECORD_LINKS_SET("content-media-record-links-set"),

    ITEM_CHANGED("item-changed"),

    ASSESSMENT_CONTENT_MUTATED("assessment-content-mutated"),

    NOTIFICATION_EVENT("notification-event"),

    FORUM_ACTIVITY("forum-activity"),

    ACHIEVEMENT_COMPLETED("achievement-completed"),

    USER_SKILL_LEVEL_CHANGED("user-skill-level-changed"),

    SKILL_ENTITY_CHANGED("skill-entity-changed");

    private final String topic;

    DaprTopic(final String topic) {
        this.topic = topic;
    }

    public String getTopic() {
        return topic;
    }

}
