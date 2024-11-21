package store.shportfolio.board.exception;



public class UserNotMatchingException extends RuntimeException {


    public UserNotMatchingException() {
        super();
    }

    public UserNotMatchingException(String message) {
        super(message);
    }

    @Override
    public String getMessage() {
        return super.getMessage();
    }
}
