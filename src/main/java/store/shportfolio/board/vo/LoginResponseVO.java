package store.shportfolio.board.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;

import java.util.Objects;
import java.util.UUID;

@Getter
public class LoginResponseVO {

    @Schema(description = "User ID generated for the user", example = "123e4567-e89b-12d3-a456-426614174000")
    private final UUID userId;

    @Schema(description = "JWT token for authentication", example = "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiIxMjM0NTY3ODkwIiwibmFtZSI6IkpvaG4gRG9lIiwiaWF0IjoxNTE2MjM5MDIyfQ.SflKxwRJSMeKKF2QT4fwpMeJf36POk6yJV_adQssw5c")
    private final String token;

    @Schema(description = "Username generated for the user", example = "hellouser")
    private final String username;

    public LoginResponseVO(UUID userId, String token, String username) {
        this.userId = userId;
        this.token = token;
        this.username = username;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        LoginResponseVO that = (LoginResponseVO) o;
        return Objects.equals(userId, that.userId) && Objects.equals(token, that.token);
    }

    @Override
    public int hashCode() {
        return Objects.hash(userId, token);
    }
}
