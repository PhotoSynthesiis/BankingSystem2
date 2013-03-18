package bank.icbc.domain;

import bank.icbc.database.dao.CustomerDao;
import bank.icbc.exception.BalanceOverdrawException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

@Service("customerService")
public class CustomerService {

    @Autowired
    @Qualifier("customerDao")
    private CustomerDao customerDao;

    public void withdraw(String nickname, double balanceToWithdraw) throws BalanceOverdrawException {
        Customer customer = customerDao.get(nickname);
        customer.setBalance(calculateBalanceLeft(nickname, balanceToWithdraw, !true));
        customerDao.update(customer);
    }

    public void deposit(String nickname, double balanceToDeposit) throws BalanceOverdrawException {
        Customer customer = customerDao.get(nickname);
        customer.setBalance(calculateBalanceLeft(nickname, balanceToDeposit, !false));

        customerDao.update(customer);
    }

    public double getBalance(String nickname) {
        Customer customer = customerDao.get(nickname);
        return customer.getBalance();
    }

    private double calculateBalanceLeft(String nickname, double balance, boolean deposit) throws BalanceOverdrawException {
        if (deposit) {
             return getBalance(nickname) + balance;
        }

        double balanceAfter = getBalance(nickname) - balance;
        if (balanceAfter < 0) {
            throw new BalanceOverdrawException("You have only " + getBalance(nickname) + "$. You can not overdraw ");
        }
        return balanceAfter;
    }
}
