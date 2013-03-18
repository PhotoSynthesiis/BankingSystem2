package bank.icbc.domain;

import bank.icbc.exception.BalanceOverdrawException;
import bank.icbc.exception.DateOfBirthInvalidException;
import bank.icbc.exception.EmailAddressInvalidException;
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
    @Qualifier("customerService")
    private CustomerService service;

    public Customer() {
        // default constructor to make the CustomerControllerTest pass
    }

    public Customer(String nickname, Date dateOfBirth, double balance, String emailAddress) throws NicknameInvalidException,
            DateOfBirthInvalidException, EmailAddressInvalidException {
        setNickname(nickname);
        setDateOfBirth(dateOfBirth);
        setBalance(balance);
        setEmailAddress(emailAddress);
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

    public void setEmailAddress(String emailAddress) throws EmailAddressInvalidException {
        if (!CustomerValidator.isEmailAddressValid(emailAddress)) {
            throw new EmailAddressInvalidException("Format of email address is invalid");
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
}
