package store.shportfolio.board.command.post;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.UUID;

@Getter
public class PostCreateResponse {


    @Schema(description = "Unique identifier of the created post", example = "123e4567-e89b-12d3-a456-426614174000")
    private final UUID postId;

    @Schema(description = "Timestamp when the post was created", example = "2024-06-18T15:30:00")
    private final LocalDateTime createdAt;


    @Builder
    public PostCreateResponse(UUID postId, LocalDateTime createdAt) {
        this.postId = postId;
        this.createdAt = createdAt;
    }
}
