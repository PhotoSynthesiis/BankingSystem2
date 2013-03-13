package bank.icbc.domain;

import bank.icbc.exception.DateOfBirthInvalidException;
import bank.icbc.exception.NicknameInvalidException;
import bank.icbc.validator.CustomerValidator;

import java.sql.Date;

public class Customer {
    private String nickname;
    private Date dateOfBirth;
    private double balance;
    private String emailAddress;

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
}
