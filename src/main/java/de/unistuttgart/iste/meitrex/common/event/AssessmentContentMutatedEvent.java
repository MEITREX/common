package de.unistuttgart.iste.meitrex.common.event;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.annotation.Nullable;
import java.util.List;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AssessmentContentMutatedEvent {
    /**
     * ID of the course the assessment belongs to.
     */
    private UUID courseId;
    /**
     * ID of the assessment this event is about.
     */
    private UUID assessmentId;
    /**
     * Type of the assessment.
     */
    private AssessmentType assessmentType;
    /**
     * List containing information about the tasks this assessment consists of.
     *
     * What exactly constitutes a "task" is implementation-specific for the different types of assessments. It should
     * just be consistent within one assessment type. E.g. for quizzes each question can be a task, and for flashcards
     * each flashcard can be a task.
     */
    private List<TaskInformation> taskInformationList;

    @Data
    @AllArgsConstructor
    public static class TaskInformation {
        /**
         * ID of this task of the assessment. Exactly what entity this ID references can be freely chosen and is
         * implementation-specific for the different types of assessments, but it should be the ID of the entity this
         * task represents, e.g. if we have a quiz and decided that each question is a separate task, then the ID of the
         * task should be the ID of the question entity in our quiz service.
         */
        private UUID taskId;
        /**
         * <p>
         * The purpose of this field is to provide the contents of this task in a simple textual format for other
         * services to use without having to have any assessment-specific code.
         * </p><p>
         * There is no exact formatting guideline for the textual representation. Its purpose is to just provide a very
         * basic means to fetch assessments’ contents in textual form, e.g. for performing a search or similar purposes.
         * </p><p>
         * The textual representation can contain spoilers, so it should never be shown to students.
         * </p><p>
         * A good textual representation may look like the following for example, for a quiz:
         * </p><p>
         * String 1:
         * <pre>
         * Question: How tall is the Eiffel Tower?
         * Correct Answer: 330m
         * </pre>
         * String 2:
         * <pre>
         * Question: What colors make up the French flag?
         * Correct Answer: blue, white, and red
         * </pre>
         * String 3:
         * <pre>
         * Task: Fill in the gaps in the text:
         * King Luis XIV famously said: "L'[etat], c'est [moi]!"
         * </pre>
         */
        private String textualRepresentation;
    }
}
