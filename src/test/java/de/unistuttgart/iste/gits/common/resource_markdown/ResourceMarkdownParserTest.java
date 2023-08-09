package de.unistuttgart.iste.gits.common.resource_markdown;

import org.junit.jupiter.api.Test;

import java.util.UUID;

import static org.assertj.core.api.Assertions.*;

class ResourceMarkdownParserTest {
    @Test
    void testParser() {
        String text = """
                Hello, this is a test [[media/4a2bbd16-866e-47ab-a413-dcd7d6152c38]].
                This shouldn't [[bla/not-a-uuid]] be parsed.
                A second media record link [[media/5d880f92-b9b6-41f5-be8c-4ba3b07bf602]] is here.
                """;

        ResourceMarkdownEntity entity = ResourceMarkdownEntity.parse(text);

        assertThat(entity.getRawText()).isEqualTo(text);
        assertThat(entity.getReferencedMediaRecordIds()).containsExactly(
                UUID.fromString("4a2bbd16-866e-47ab-a413-dcd7d6152c38"),
                UUID.fromString("5d880f92-b9b6-41f5-be8c-4ba3b07bf602")
        );
    }
}
