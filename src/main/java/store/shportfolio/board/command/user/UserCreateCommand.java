package store.shportfolio.board.command.user;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;

@Getter
@ToString
@Setter
@NoArgsConstructor
public class UserCreateCommand {


    @NotBlank(message = "Username is mandatory")
    @Size(min = 4, max = 12, message = "Username must be between 4 and 12 characters")
    @Schema(description = "Username of the user", example = "john_doe")
    private String username;

    @NotBlank(message = "Password is mandatory")
    @Size(min = 8, max = 20, message = "Password must be between 8 and 20 characters")
    @Schema(description = "Password of the user", example = "password123")
    private String password;

    @Schema(description = "Email address of the user", example = "user@example.com")
    private String email;

    @Builder
    public UserCreateCommand(String username, String password, String email) {
        this.username = username;
        this.password = password;
        this.email = email;
    }
}
