package de.unistuttgart.iste.meitrex.common.event;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.annotation.Nullable;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MediaRecordInfoEvent {
    @NotNull
    private UUID mediaRecordId;
    @NotNull
    private MediaType mediaType;
    @Nullable
    private Float durationSeconds;
    @Nullable
    private Integer pageCount;
}
