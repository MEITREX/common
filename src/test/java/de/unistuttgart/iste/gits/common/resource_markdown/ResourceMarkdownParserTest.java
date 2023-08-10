package de.unistuttgart.iste.gits.common.resource_markdown;

import de.unistuttgart.iste.gits.generated.dto.ResourceMarkdownInput;
import org.junit.jupiter.api.Test;

import java.util.UUID;

import static org.assertj.core.api.Assertions.*;

class ResourceMarkdownParserTest {
    @Test
    void testParser() {
        String text = """
                Hello, this is a test [[media/4a2bbd16-866e-47ab-a413-dcd7d6152c38]].
                This shouldn't [[bla/not-a-uuid]] be parsed at all.
                Valid uuid but invalid resource type [[invalid/d2bb1c20-f4c9-4ced-a345-cdb21d2e2657]] shouldn't be matched.
                A second media record link [[media/5d880f92-b9b6-41f5-be8c-4ba3b07bf602]] is here.
                """;

        ResourceMarkdownEntity entity = new ResourceMarkdownEntity(text);

        assertThat(entity.getText()).isEqualTo(text);
        assertThat(entity.getReferencedMediaRecordIds()).containsExactly(
                UUID.fromString("4a2bbd16-866e-47ab-a413-dcd7d6152c38"),
                UUID.fromString("5d880f92-b9b6-41f5-be8c-4ba3b07bf602")
        );
    }
}
