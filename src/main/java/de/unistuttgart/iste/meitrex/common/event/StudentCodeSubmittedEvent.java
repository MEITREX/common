package de.unistuttgart.iste.meitrex.common.event;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.OffsetDateTime;
import java.util.Map;
import java.util.UUID;

/**
 * Event published when a student submits code for an assignment.
 * 
 * This event is triggered by the assignment service when it loads and processes
 * a student's code submission from their repository. The event contains information
 * about the submission including the repository details, commit information, and
 * the submitted files.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class StudentCodeSubmittedEvent {

    /**
     * ID of the student who submitted the code.
     */
    private UUID studentId;

    /**
     * ID of the assignment for which the code was submitted.
     */
    private UUID assignmentId;

    /**
     * ID of the course the assignment belongs to.
     */
    private UUID courseId;

    /**
     * URL of the student's repository containing the submitted code.
     */
    private String repositoryUrl;

    /**
     * SHA hash of the commit that was submitted.
     */
    private String commitSha;

    /**
     * Timestamp when the commit was created.
     */
    private OffsetDateTime commitTimestamp;

    /**
     * Map of file paths to their contents from the submission.
     * The key is the file path relative to the repository root,
     * and the value is the file content.
     */
    private Map<String, String> files;

    /**
     * Name of the branch from which the code was submitted.
     */
    private String branch;
}
