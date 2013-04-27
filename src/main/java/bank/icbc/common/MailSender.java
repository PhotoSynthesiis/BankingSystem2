package bank.icbc.common;

import bank.icbc.domain.CustomMailMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.stereotype.Component;

@Component("mailSender")
public class MailSender {
    @Autowired
    private JavaMailSenderImpl mailSender;

    public void sendEmail(CustomMailMessage message) {
        mailSender.send(message);
    }
}
