package store.shportfolio.board.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Getter
@Setter
@ToString
@NoArgsConstructor
public class VerifyCodeVO {

    @Schema(description = "User Email", example = "user@example.com")
    @Email
    @NotNull
    private String email;

    @Schema(description = "User account activation code", example = "584262")
    @NotNull
    private String code;

    @Builder
    public VerifyCodeVO(String email, String code) {
        this.email = email;
        this.code = code;
    }
}
