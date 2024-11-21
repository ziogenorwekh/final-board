package store.shportfolio.board.command.post;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Getter
@NoArgsConstructor
public class PostTrackQuery {

    @NotNull
    @Schema(description = "Unique identifier of the post to track", example = "123e4567-e89b-12d3-a456-426614174001")
    private UUID postId;
    @Builder
    public PostTrackQuery(UUID postId) {
        this.postId = postId;
    }
}
