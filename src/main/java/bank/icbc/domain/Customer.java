package bank.icbc.domain;

import bank.icbc.exception.BalanceOverdrawException;
import bank.icbc.exception.CustomerException;
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
    private Date joinDate;

    @Autowired
    @Qualifier("customerService")
    private CustomerService service;
    private boolean premium;

    // default constructor to make the CustomerControllerTest pass
    public Customer() {
    }

    public Customer(String nickname, Date dateOfBirth, double balance, String emailAddress, boolean isPremium, Date joinDate) throws CustomerException {
        setNickname(nickname);
        setDateOfBirth(dateOfBirth);
        setBalance(balance);
        setEmailAddress(emailAddress);
        setPremium(isPremium);
        setJoinDate(joinDate);
    }

    public void setNickname(String nickname) throws CustomerException {

        if (!CustomerValidator.isNicknameValid(nickname)) {
            throw new CustomerException("Nickname should be combination of lower case of letters and digits (empty value or space is not allowed)");
        }

        this.nickname = nickname;
    }

    public void setDateOfBirth(Date dateOfBirth) throws CustomerException {
        if (!CustomerValidator.isDateOfBirthValid(dateOfBirth)) {
            throw new CustomerException("Date of birth is invalid");
        }
        this.dateOfBirth = dateOfBirth;
    }

    public void setEmailAddress(String emailAddress) throws CustomerException {
        if (!CustomerValidator.isEmailAddressValid(emailAddress)) {
            throw new CustomerException("Format of email address is invalid");
        }
        this.emailAddress = emailAddress;
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

    public String getNickname() {
        return nickname;
    }

    public Date getDateOfBirth() {
        return dateOfBirth;
    }

    public void deposit(double balance, String nickname) throws BalanceOverdrawException {
        service.deposit(nickname, balance);
    }

    public void withdraw(double balance, String nickname) throws BalanceOverdrawException {
        service.withdraw(nickname, balance);
    }

    public boolean isPremium() {
        return premium;
    }

    public void setPremium(boolean premium) {
        this.premium = premium;
    }

    public Date getJoinDate() {
        return joinDate;
    }

    public void setJoinDate(Date joinDate) {
        this.joinDate = joinDate;
    }
}
