package de.unistuttgart.iste.gits.common.resource_markdown;

import jakarta.persistence.*;
import lombok.*;

import java.util.List;
import java.util.UUID;

/**
 * Wrapper class for the ResourceMarkdownEmbeddable in case it is easier to use
 * as an entity, e.g., when multiple markdowns are referenced by the same entity.
 * <br />
 * As this is less performant than the embeddable, it should only be used if necessary.
 * <br />
 * You need to add the following annotation to your application class:
 * {@code @EntityScan({"de.unistuttgart.iste.gits.common.resource_markdown", <your entity package>})}
 */
@Entity(name = "ResourceMarkdown")
@Data
@Builder
@AllArgsConstructor
public class ResourceMarkdownEntity {

    @Id
    @GeneratedValue
    @EqualsAndHashCode.Exclude
    private UUID id;

    @Embedded
    private ResourceMarkdownEmbeddable resourceMarkdown;

    public ResourceMarkdownEntity() {
        this.resourceMarkdown = new ResourceMarkdownEmbeddable();
    }

    public ResourceMarkdownEntity(ResourceMarkdownEmbeddable resourceMarkdown) {
        this.resourceMarkdown = resourceMarkdown;
    }

    public ResourceMarkdownEntity(String text) {
        this.resourceMarkdown = new ResourceMarkdownEmbeddable(text);
    }

    public String getText() {
        return this.resourceMarkdown.getText();
    }

    public List<UUID> getReferencedMediaRecordIds() {
        return this.resourceMarkdown.getReferencedMediaRecordIds();
    }

    public void setText(String text) {
        this.resourceMarkdown.setText(text);
    }

    public void setReferencedMediaRecordIds(List<UUID> referencedMediaRecordIds) {
        this.resourceMarkdown.setReferencedMediaRecordIds(referencedMediaRecordIds);
    }
}
