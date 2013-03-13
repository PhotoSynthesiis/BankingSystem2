package bank.icbc.domain;

import bank.icbc.exception.NicknameInvalidException;
import bank.icbc.validator.CustomerValidator;

import java.sql.Date;

public class Customer {
    private String nickname;
    private Date dateOfBirth;
    private double balance;
    private String emailAddress;

    public void setNickname(String nickname) throws NicknameInvalidException {

        if (!CustomerValidator.isNicknameValid(nickname)) {
            throw new NicknameInvalidException("Nickname should be combination of lower case of letters and digits (empty value or space is not allowed)");
        }

        this.nickname = nickname;
    }

    public void setDateOfBirth(Date dateOfBirth) {
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
