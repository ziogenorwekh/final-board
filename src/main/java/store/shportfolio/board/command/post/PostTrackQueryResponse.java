package store.shportfolio.board.command.post;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.UUID;

@Getter
public class PostTrackQueryResponse {

    @Schema(description = "Unique identifier of the post", example = "123e4567-e89b-12d3-a456-426614174000")
    private final UUID postId;

    @Schema(description = "Unique identifier of the user who created the post", example = "123e4567-e89b-12d3-a456-426614174001")
    private final UUID userId;

    @Schema(description = "Username of the user who created the post", example = "john_doe")
    private final String poster;

    @Schema(description = "Title of the post", example = "Sample Post Title")
    private final String title;

    @Schema(description = "Content of the post", example = "This is a sample post content.")
    private final String contents;

    @Schema(description = "Timestamp when the post was created")
    private final LocalDateTime createdAt;

    @Schema(description = "Timestamp when the post was last updated")
    private final LocalDateTime updatedAt;

    @Builder
    public PostTrackQueryResponse(UUID postId, UUID userId, String poster,
                                  String title, String contents,
                                  LocalDateTime createdAt, LocalDateTime updatedAt) {
        this.postId = postId;
        this.userId = userId;
        this.poster = poster;
        this.title = title;
        this.contents = contents;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }
}
