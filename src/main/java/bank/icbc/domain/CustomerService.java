package bank.icbc.domain;

import bank.icbc.database.dao.CustomerDao;
import bank.icbc.exception.BalanceOverdrawException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import static bank.icbc.domain.TransactionType.DEPOSIT;
import static bank.icbc.domain.TransactionType.WITHDRAW;

@Service("customerService")
public class CustomerService {

    @Autowired
    @Qualifier("customerDao")
    private CustomerDao customerDao;

    public void withdraw(String nickname, double balanceToWithdraw) throws BalanceOverdrawException {
        Customer customer = customerDao.get(nickname);
        customer.setBalance(calculateBalanceLeft(nickname, balanceToWithdraw, WITHDRAW));

        customerDao.update(customer);
    }

    public void deposit(String nickname, double balanceToDeposit) throws BalanceOverdrawException {
        Customer customer = customerDao.get(nickname);

        customer.setBalance(calculateBalanceLeft(nickname, balanceToDeposit, DEPOSIT));

        if (customer.getBalance() > 40000) {
            customer.setPremium(true);
        }

        customerDao.update(customer);
    }

    public double getBalance(String nickname) {
        Customer customer = customerDao.get(nickname);
        return customer.getBalance();
    }

    private double calculateBalanceLeft(String nickname, double balance, TransactionType transactionType) throws BalanceOverdrawException {
        if (transactionType.equals(DEPOSIT)) {
            return getBalance(nickname) + balance;
        }

        return handleWithdraw(nickname, balance);
    }

    private double handleWithdraw(String nickname, double balance) throws BalanceOverdrawException {
        double balanceAfter = getBalance(nickname) - balance;
        if (balanceAfter < 0)

        {
            throw new BalanceOverdrawException("You have only " + getBalance(nickname) + "$. You can not overdraw ");
        }

        return balanceAfter;
    }
}
