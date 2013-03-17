package bank.icbc.exception;

public class EmailAddressInvalidException extends Throwable {
    public EmailAddressInvalidException(String msg) {
        super(msg);
    }
}
