package store.shportfolio.board.exception;

public class PostNotOwnerException extends RuntimeException {
    public PostNotOwnerException(String message) {
        super(message);
    }

    public PostNotOwnerException(String message, Throwable cause) {
        super(message, cause);
    }
}
