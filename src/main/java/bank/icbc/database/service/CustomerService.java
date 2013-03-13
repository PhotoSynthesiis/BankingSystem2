package bank.icbc.database.service;

import bank.icbc.database.dao.CustomerDao;
import bank.icbc.domain.Customer;
import bank.icbc.domain.MailSender;
import bank.icbc.exception.BalanceOverdrawException;
import bank.icbc.exception.CustomerNotFoundException;
import bank.icbc.exception.DuplicateCustomerException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;

@Service("customerService")
public class CustomerService {

    @Autowired
    @Qualifier("customerDao")
    private CustomerDao customerDao;

    @Autowired
    private MailSender mailSender;

    public void addCustomer(Customer customer) throws DuplicateCustomerException {
        try {
            customerDao.addCustomer(customer);
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
            return customerDao.getCustomer(nickname);
        } catch (EmptyResultDataAccessException exception) {
            throw new CustomerNotFoundException("Customer with nickname " + nickname + " is not found");
        }
    }

    public void withdraw(String nickname, double balanceToWithdraw) throws BalanceOverdrawException {
        double balanceBefore = customerDao.getBalance(nickname);
        double balanceAfter = balanceBefore - balanceToWithdraw;

        if (balanceAfter < 0) {
            throw new BalanceOverdrawException("You have only " + balanceBefore + "$. You can not withdraw " + balanceToWithdraw + "$");
        }
        customerDao.handleBalance(nickname, balanceAfter);
    }

    public void deposit(String nickname, double balanceToDeposit) {
        double balanceBefore = getBalance(nickname);
        double balanceAfter = balanceBefore + balanceToDeposit;

        customerDao.handleBalance(nickname, balanceAfter);
    }

    public double getBalance(String nickname) {
        return customerDao.getBalance(nickname);
    }
}
