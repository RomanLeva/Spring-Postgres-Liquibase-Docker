package group.demoapp.aspect.exception;

public class RequestJsonException extends AbstractException {
    public RequestJsonException(String message) {
        this.message = message;
    }
    @Override
    public String getMessage() {
        return message;
    }
}
