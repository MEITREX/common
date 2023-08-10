package de.unistuttgart.iste.gits.common.resource_markdown;

import de.unistuttgart.iste.gits.generated.dto.ResourceMarkdownInput;
import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * An entity to store ResourceMarkdown text.
 * <br />
 * For more information on ResourceMarkdown see
 * <a href="https://gits-enpro.readthedocs.io/en/latest/dev-manuals/api-specifications/ResourceMarkdown.html">
 *     the specification
 * </a>.
 */
@Data
@Builder
@Embeddable
@NoArgsConstructor
@AllArgsConstructor
public class ResourceMarkdownEntity {
    private String rawText;
    private List<UUID> referencedMediaRecordIds;

    /**
     * Parses a string containing ResourceMarkdown to a new ResourceMarkdownEntity instance.
     *
     * @param text The text to parse.
     * @return Returns a ResourceMarkdownEntity containing the parsed data from the input text.
     */
    public static ResourceMarkdownEntity fromString(String text) {
        // quite a complicated regex pattern to match a ResourceMarkdown link. For an explanation of this regex, see
        // the specification document of ResourceMarkdown:
        // https://gits-enpro.readthedocs.io/en/latest/dev-manuals/api-specifications/ResourceMarkdown.html
        // The only thing added here are additional backslashes for
        // Java string escaping, and two pairs of braces "( )" to create two capture groups, one for the resource type
        // name (the string before the "/") and the other one for the UUID after the "/"
        Pattern pattern = Pattern.compile(
                "\\[\\[" +
                "([a-zA-Z]+)" +
                "/" +
                "([0-9a-fA-F]{8}-[0-9a-fA-F]{4}-[0-9a-fA-F]{4}-[0-9a-fA-F]{4}-[0-9a-fA-F]{12})" +
                "\\]\\]");

        Matcher matcher = pattern.matcher(text);

        List<UUID> referencedMediaRecordIds = new ArrayList<>();

        // find all matches in the input text
        while(matcher.find()) {
            String resourceType = matcher.group(1);
            UUID resourceId = UUID.fromString(matcher.group(2));

            // We don't throw an error and instead silently ignore it if an invalid resourceType is encountered
            // because for markdown parsing, something invalid is just treated as plain text
            switch(resourceType) {
                case "media" -> referencedMediaRecordIds.add(resourceId);
            }
        }

        return ResourceMarkdownEntity.builder()
                .rawText(text)
                .referencedMediaRecordIds(referencedMediaRecordIds)
                .build();
    }

    public static ResourceMarkdownEntity fromResourceMarkdownInput(ResourceMarkdownInput input) {
        return ResourceMarkdownEntity.fromString(input.getRawText());
    }
}
