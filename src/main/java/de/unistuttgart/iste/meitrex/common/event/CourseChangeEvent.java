package de.unistuttgart.iste.meitrex.common.event;

import lombok.*;

import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CourseChangeEvent {

    private UUID courseId;
    private CrudOperation operation;
}
