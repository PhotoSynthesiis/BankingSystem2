package bank.icbc.domain;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSenderImpl;

public class MailSender {
    @Autowired
    private JavaMailSenderImpl javaMailSenderImpl;

    public void sendEmail(String nickname) {
        SimpleMailMessage mailMessage = new SimpleMailMessage();

        mailMessage.setTo(nickname + "@thebank.com");
        mailMessage.setFrom("admin@thebank.com");
        mailMessage.setSubject("Welcome!");
        mailMessage.setText("Dear " + nickname + ", welcome to the bank");

        javaMailSenderImpl.send(mailMessage);

    }
}
