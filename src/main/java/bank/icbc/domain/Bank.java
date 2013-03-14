package bank.icbc.domain;

import bank.icbc.database.dao.CustomerDao;
import bank.icbc.exception.BalanceOverdrawException;
import bank.icbc.exception.CustomerNotFoundException;
import bank.icbc.exception.DuplicateCustomerException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;

@Service("bank")
public class Bank {

    @Autowired
    @Qualifier("customerDao")
    private CustomerDao customerDao;

    @Autowired
    private MailSender mailSender;

    public void addCustomer(Customer customer) throws DuplicateCustomerException {
        try {
            customerDao.add(customer);
//            sendEmailToCustomer(customer);
        } catch (DuplicateKeyException exception) {
            throw new DuplicateCustomerException("Customer with nickname " + customer.getNickname() + " has already existed");
        }
    }

    private void sendEmailToCustomer(Customer customer) {
        mailSender.sendEmail(customer.getNickname(), customer.getEmailAddress());
    }

    public Customer getCustomer(String nickname) throws CustomerNotFoundException {
        try {
            return customerDao.get(nickname);
        } catch (EmptyResultDataAccessException exception) {
            throw new CustomerNotFoundException("Customer with nickname " + nickname + " is not found");
        }
    }

    public void withdraw(String nickname, double balanceToWithdraw) throws BalanceOverdrawException {
        double balanceLeft = calculateBalanceLeft(nickname, balanceToWithdraw, true);

        Customer customer = customerDao.get(nickname);
        customer.setBalance(balanceLeft);

        customerDao.update(customer);
    }

    private double calculateBalanceLeft(String nickname, double balance, boolean withdraw) throws BalanceOverdrawException {
        double balanceBefore = getBalance(nickname);

        double balanceAfter;
        if (withdraw) {
            balanceAfter = balanceBefore - balance;
        } else {
            balanceAfter = balanceBefore + balance;
        }

        if (balanceAfter < 0) {
            throw new BalanceOverdrawException("You have only " + balanceBefore + "$. You can not overdraw ");
        }
        return balanceAfter;
    }

    public void deposit(String nickname, double balanceToDeposit) throws BalanceOverdrawException {
        double balance = calculateBalanceLeft(nickname, balanceToDeposit, false);
        Customer customer = customerDao.get(nickname);
        customer.setBalance(balance);

        customerDao.update(customer);
    }

    public double getBalance(String nickname) {
        Customer customer = customerDao.get(nickname);
        return customer.getBalance();
    }
}
