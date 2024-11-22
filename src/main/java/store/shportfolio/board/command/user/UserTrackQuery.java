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
    @Schema(description = "Username of the user", example = "john_doe")
    private String username;
    @Builder
    public UserTrackQuery(String username) {
        this.username = username;
    }
}
