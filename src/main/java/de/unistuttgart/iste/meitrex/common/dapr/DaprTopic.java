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

    ASSESSMENT_CONTENT_MUTATED("assessment-content-mutated");

    private final String topic;

    DaprTopic(final String topic) {
        this.topic = topic;
    }

    public String getTopic() {
        return topic;
    }

}
