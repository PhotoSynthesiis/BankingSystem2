package bank.icbc.exception;

public class DuplicateCustomerException extends Exception {
    public DuplicateCustomerException(String msg) {
        super(msg);
    }
}
