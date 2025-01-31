package group.demoapp.aspect.exception;

public class UserException extends AbstractException {
    public UserException(String message) {
        this.message = message;
    }

    @Override
    public String getMessage() {
        return message;
    }
}
