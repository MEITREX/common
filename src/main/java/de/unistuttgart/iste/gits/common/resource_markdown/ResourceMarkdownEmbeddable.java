package de.unistuttgart.iste.gits.common.resource_markdown;

import jakarta.persistence.*;
import lombok.*;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * An entity to store ResourceMarkdown text.
 * <br />
 * For more information on ResourceMarkdown see
 * <a href="https://gits-enpro.readthedocs.io/en/latest/dev-manuals/api-specifications/ResourceMarkdown.html">
 * the specification
 * </a>.
 */
@Getter
@EqualsAndHashCode
@ToString
@Embeddable
public class ResourceMarkdownEmbeddable {

    @Column(columnDefinition = "TEXT")
    private String text;
    @ElementCollection
    @EqualsAndHashCode.Exclude
    private List<UUID> referencedMediaRecordIds;

    // quite a complicated regex pattern to match a ResourceMarkdown link. For an explanation of this regex, see
    // the specification document of ResourceMarkdown:
    // https://gits-enpro.readthedocs.io/en/latest/dev-manuals/api-specifications/ResourceMarkdown.html
    // The only thing added here are additional backslashes for
    // Java string escaping, and two pairs of braces "( )" to create two capture groups, one for the resource type
    // name (the string before the "/") and the other one for the UUID after the "/"
    private static final Pattern PATTERN = Pattern.compile(
            "\\[\\[" +
            "([a-zA-Z]+)" +
            "/" +
            "([0-9a-fA-F]{8}-[0-9a-fA-F]{4}-[0-9a-fA-F]{4}" +
            "-[0-9a-fA-F]{4}-[0-9a-fA-F]{12})" +
            "]]");

    public ResourceMarkdownEmbeddable() {
        setText("");
    }

    public ResourceMarkdownEmbeddable(String text) {
        setText(text);
    }

    /**
     * Sets the Markdown text of this entity and parses it and populates related fields with the parsed data.
     *
     * @param text The new Markdown text this entity should have.
     */
    public void setText(String text) {
        this.text = text;

        Matcher matcher = PATTERN.matcher(text);

        List<UUID> referencedMediaRecords = new ArrayList<>();

        // find all matches in the input text
        while (matcher.find()) {
            String resourceType = matcher.group(1);
            UUID resourceId = UUID.fromString(matcher.group(2));

            switch (resourceType) {
                case "media" -> referencedMediaRecords.add(resourceId);
                case "content" -> {
                    // not implemented yet, add other resource types here too
                }
                default -> {
                    // do nothing
                    // We don't throw an error and instead silently ignore it if an invalid resourceType is encountered
                    // because for markdown parsing, something invalid is just treated as plain text
                }
            }
        }

        this.referencedMediaRecordIds = referencedMediaRecords;
    }


}
