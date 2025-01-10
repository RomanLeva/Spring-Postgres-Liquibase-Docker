package group.demoapp.aspect.exception;

public class WalletException extends AbstractException {
    public WalletException(String message) {
        this.message = message;
    }

    @Override
    public String getMessage() {
        return message;
    }
}
