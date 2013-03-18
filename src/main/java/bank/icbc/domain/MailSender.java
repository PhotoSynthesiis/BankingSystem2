package bank.icbc.domain;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSenderImpl;

import static java.lang.String.format;

public class MailSender {
    @Autowired
    private JavaMailSenderImpl mailSender;

    public void sendEmail(String nickname, String emailAddress) {
        mailSender.send(buildMessage(nickname, emailAddress));
    }

    private SimpleMailMessage buildMessage(String nickname, String emailAddress) {
        SimpleMailMessage mailMessage = new SimpleMailMessage();

        mailMessage.setTo(emailAddress);
        mailMessage.setFrom("admin@thebank.com");
        mailMessage.setSubject("Welcome!");
        mailMessage.setText(format("Dear %s, welcome to the bank", nickname));
        return mailMessage;
    }
}
