package store.shportfolio.board.command.post;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.UUID;

@Getter
@NoArgsConstructor
public class PostCreateCommand {

    @Schema(description = "Title of the post", example = "Sample Title")
    @NotBlank(message = "Title must not be blank")
    @Size(max = 255, message = "Title must not exceed 255 characters")
    private String title;

    @Schema(description = "Content of the post", example = "Sample content for the post")
    @NotBlank(message = "Content must not be blank")
    private String content;

    @Schema(description = "User ID of the post creator", example = "123e4567-e89b-12d3-a456-426614174000")
    @NotNull
    @Setter
    private UUID userId;

    @Builder
    public PostCreateCommand(String title, String content, UUID userId) {
        this.title = title;
        this.content = content;
        this.userId = userId;
    }

}
