package de.unistuttgart.iste.meitrex.common.dapr;

public enum DaprTopic {
    COURSE_CHANGED("course-changed"),
    CHAPTER_CHANGED("chapter-changed"),

    CONTENT_PROGRESSED("content-progressed"),

    CONTENT_CHANGED("content-changed"),

    USER_PROGRESS_UPDATED("user-progress-updated");


    private final String topic;

    DaprTopic(final String topic) {
        this.topic = topic;
    }

    public String getTopic() {
        return topic;
    }

}
