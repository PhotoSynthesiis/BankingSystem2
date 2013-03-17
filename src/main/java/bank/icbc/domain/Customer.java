package bank.icbc.domain;

import bank.icbc.database.dao.CustomerDao;
import bank.icbc.exception.BalanceOverdrawException;
import bank.icbc.exception.DateOfBirthInvalidException;
import bank.icbc.exception.NicknameInvalidException;
import bank.icbc.validator.CustomerValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.sql.Date;

@Service("customer")
public class Customer {
    private String nickname;
    private Date dateOfBirth;
    private double balance;
    private String emailAddress;

    @Autowired
    @Qualifier("customerDao")
    private CustomerDao customerDao;

    public Customer() {
        // default constructor to make the CustomerControllerTest pass
    }

    public Customer(String nickname, Date dateOfBirth, double balance) throws NicknameInvalidException, DateOfBirthInvalidException {
        setNickname(nickname);
        setDateOfBirth(dateOfBirth);
        setBalance(balance);
    }

    public void setNickname(String nickname) throws NicknameInvalidException {

        if (!CustomerValidator.isNicknameValid(nickname)) {
            throw new NicknameInvalidException("Nickname should be combination of lower case of letters and digits (empty value or space is not allowed)");
        }

        this.nickname = nickname;
    }

    public void setDateOfBirth(Date dateOfBirth) throws DateOfBirthInvalidException {
        if (!CustomerValidator.isDateOfBirthValid(dateOfBirth)) {
            throw new DateOfBirthInvalidException("Date of birth is invalid");
        }
        this.dateOfBirth = dateOfBirth;
    }

    public String getNickname() {
        return nickname;
    }

    public Date getDateOfBirth() {
        return dateOfBirth;
    }

    public void setBalance(double balance) {
        this.balance = balance;
    }

    public double getBalance() {
        return balance;
    }

    public String getEmailAddress() {
        return emailAddress;
    }

    private double calculateBalanceLeft(String nickname, double balance, boolean withdraw) throws BalanceOverdrawException {
        double balanceBefore = getBalanceInAccount(nickname);

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

    public double getBalanceInAccount(String nickname) {
        Customer customer = customerDao.get(nickname);
        return customer.getBalance();
    }

    public void deposit(String nickname, double balanceToDeposit) throws BalanceOverdrawException {
        double balance = calculateBalanceLeft(nickname, balanceToDeposit, false);
        Customer customer = customerDao.get(nickname);
        customer.setBalance(balance);

        customerDao.update(customer);
    }

    public void withdraw(String nickname, double balanceToWithdraw) throws BalanceOverdrawException {
        double balanceLeft = calculateBalanceLeft(nickname, balanceToWithdraw, true);

        Customer customer = customerDao.get(nickname);
        customer.setBalance(balanceLeft);

        customerDao.update(customer);
    }
}
