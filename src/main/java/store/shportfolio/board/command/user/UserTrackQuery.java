package store.shportfolio.board.command.user;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Getter
@NoArgsConstructor
public class UserTrackQuery {

    @NotNull
    @Schema(description = "User ID", example = "123e4567-e89b-12d3-a456-426614174000", required = true)
    private UUID userId;
    @Builder
    public UserTrackQuery(UUID userId) {
        this.userId = userId;
    }
}
