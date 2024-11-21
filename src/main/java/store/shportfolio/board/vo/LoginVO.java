package store.shportfolio.board.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@ToString
@Getter
@NoArgsConstructor
public class LoginVO {

    @NotBlank(message = "Username must not be blank")
    @Size(min = 4, max = 20, message = "Username length must be between 4 and 20 characters")
    @Schema(description = "Username for authentication", example = "john_doe")
    private String username;

    @NotBlank(message = "Password must not be blank")
    @Size(min = 6, max = 30, message = "Password length must be between 6 and 30 characters")
    @Schema(description = "Password for authentication", example = "password123")
    private String password;

    public LoginVO(String username, String password) {
        this.username = username;
        this.password = password;
    }
}
