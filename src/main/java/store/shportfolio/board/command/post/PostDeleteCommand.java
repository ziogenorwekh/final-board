package store.shportfolio.board.command.post;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.UUID;

@Getter
@NoArgsConstructor
public class PostDeleteCommand {

    @NotNull
    @Setter
    @Schema(description = "User ID of the user who is deleting the post", example = "123e4567-e89b-12d3-a456-426614174000")
    private UUID userId;

    @NotNull
    @Schema(description = "Unique identifier of the post to be deleted", example = "123e4567-e89b-12d3-a456-426614174001")
    @Setter
    private UUID postId;
    @Builder
    public PostDeleteCommand(UUID userId, UUID postId) {
        this.userId = userId;
        this.postId = postId;
    }
}
