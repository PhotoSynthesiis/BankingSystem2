package bank.icbc.exception;

public class BalanceOverdrawException extends Exception{
    public BalanceOverdrawException(String msg) {
        super(msg);
    }
}
