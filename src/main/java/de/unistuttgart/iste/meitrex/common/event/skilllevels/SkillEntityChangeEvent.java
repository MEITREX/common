package de.unistuttgart.iste.meitrex.common.event.skilllevels;

import de.unistuttgart.iste.meitrex.common.event.CrudOperation;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SkillEntityChangeEvent {
    private UUID skillId;
    private String skillName;
    private String skillCategory;
    private CrudOperation operation;
}
