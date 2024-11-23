package store.shportfolio.board.command.user;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.UUID;

@Getter
@NoArgsConstructor
public class UserUpdateCommand {

    @Setter
    @Schema(description = "User ID", example = "123e4567-e89b-12d3-a456-426614174000")
    private UUID userId;

    @Schema(description = "Current password", required = true)
    @NotBlank(message = "Current password is mandatory")
    @Size(min = 8, max = 20, message = "Password must be between 8 and 20 characters")
    private String currentPassword;

    @Schema(description = "New password", required = true)
    @NotBlank(message = "New password is mandatory")
    @Size(min = 8, max = 20, message = "Password must be between 8 and 20 characters")
    private String newPassword;

    @Builder
    public UserUpdateCommand(String currentPassword, String newPassword) {
        this.currentPassword = currentPassword;
        this.newPassword = newPassword;
    }
}
