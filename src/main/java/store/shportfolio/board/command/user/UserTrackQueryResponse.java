package store.shportfolio.board.command.user;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.UUID;

@Getter
public class UserTrackQueryResponse {

    @Schema(description = "User ID")
    private final UUID userId;

    @Schema(description = "Username")
    private final String username;

    @Schema(description = "Email address")
    private final String email;

    @Schema(description = "User creation timestamp")
    private final LocalDateTime createdAt;

    @Builder
    public UserTrackQueryResponse(UUID userId, String username, String email, LocalDateTime createdAt) {
        this.userId = userId;
        this.username = username;
        this.email = email;
        this.createdAt = createdAt;
    }
}
