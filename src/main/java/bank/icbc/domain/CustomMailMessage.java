package bank.icbc.domain;

import org.springframework.mail.SimpleMailMessage;

public class CustomMailMessage extends SimpleMailMessage {
    public void setToAddress(String toAddress) {
        setTo(toAddress);
    }

    public void setFromAddress(String fromAddress) {
        setFrom(fromAddress);
    }

    public void setEmailSubject(String subject) {
        setSubject(subject);
    }

    public void setEmailContent(String content) {
        setText(content);
    }

    public String getToAddress() {
        return getTo()[0];
    }

    public String getFromAddress() {
        return getFrom();
    }

    public String getEmailSubject() {
        return getSubject();
    }

    public String getEmailContent() {
        return getText();
    }
}
