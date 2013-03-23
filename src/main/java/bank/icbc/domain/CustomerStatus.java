package bank.icbc.domain;

public class CustomerStatus {
    private boolean emailToManagerSent;
    private String nickname;

    public CustomerStatus(String nickname, boolean emailToManagerSent) {
        this.nickname = nickname;
        this.emailToManagerSent = emailToManagerSent;
    }

    public boolean isEmailToManagerSent() {
        return emailToManagerSent;
    }

    public void setEmailToManagerSent(boolean emailToManagerSent) {
        this.emailToManagerSent = emailToManagerSent;
    }

    public String getNickname() {
        return nickname;
    }
}
