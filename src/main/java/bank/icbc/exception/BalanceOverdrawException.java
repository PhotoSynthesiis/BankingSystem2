package bank.icbc.exception;

public class BalanceOverdrawException extends RuntimeException{
    public BalanceOverdrawException(String msg) {
        super(msg);
    }
}
