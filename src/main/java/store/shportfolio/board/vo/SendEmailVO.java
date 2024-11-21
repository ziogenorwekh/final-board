package store.shportfolio.board.vo;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Getter
@Setter
@ToString
@NoArgsConstructor
public class SendEmailVO {

    @Parameter(description = "User email")
    @Email(message = "이메일 형식이어야 합니다.")
    @NotNull(message = "이메일을 입력해야 합니다.")
    @Schema(description = "User Email", example = "test@example.com")
    @JsonProperty("email")
    private String email;

    public SendEmailVO(String email) {
        this.email = email;
    }
}
