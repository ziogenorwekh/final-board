package store.shportfolio.board.vo;

import lombok.Builder;
import lombok.Getter;

@Getter
public class ExceptionResponseVO {


    private String message;

    @Builder
    public ExceptionResponseVO(String message) {
        this.message = message;
    }
}
