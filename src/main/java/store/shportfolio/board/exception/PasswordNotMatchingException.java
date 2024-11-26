package store.shportfolio.board.exception;

public class PasswordNotMatchingException extends RuntimeException {

    public PasswordNotMatchingException(String message) {
        super(message);
    }

    public PasswordNotMatchingException(String message, Throwable cause) {
        super(message, cause);
    }
}
