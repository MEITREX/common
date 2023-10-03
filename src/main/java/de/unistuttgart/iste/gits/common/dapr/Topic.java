package de.unistuttgart.iste.gits.common.dapr;

public enum Topic {
    COURSE_CHANGED("course-changed"),
    CHAPTER_CHANGED("chapter-changed"),

    CONTENT_PROGRESSED("content-progressed"),

    CONTENT_CHANGED("content-changed"),

    USER_PROGRESS_UPDATED("user-progress-updated");


    private final String topic;

    Topic(final String topic) {
        this.topic = topic;
    }

    public String getTopic() {
        return topic;
    }

}
