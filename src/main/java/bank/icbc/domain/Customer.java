package bank.icbc.domain;

import java.sql.Date;

public class Customer {
    private String nickname;
    private Date dateOfBirth;

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public void setDateOfBirth(Date dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }

    public String getNickname() {
        return nickname;
    }
}
