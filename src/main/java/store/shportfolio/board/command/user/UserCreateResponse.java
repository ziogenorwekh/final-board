package store.shportfolio.board.command.user;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.UUID;

@Getter
public class UserCreateResponse {

    @Schema(description = "User ID", example = "123e4567-e89b-12d3-a456-426614174000")
    private final UUID userId;

    @Schema(description = "Username of the user", example = "john_doe")
    private final String username;

    @Schema(description = "Email address of the user", example = "user@example.com")
    private final String email;

    @Schema(description = "Date and time when the user was created", example = "2023-06-18T10:15:30")
    private final LocalDateTime createdAt;

    @Builder
    public UserCreateResponse(UUID userId, String username, String email, LocalDateTime createdAt) {
        this.userId = userId;
        this.username = username;
        this.email = email;
        this.createdAt = createdAt;
    }
}
