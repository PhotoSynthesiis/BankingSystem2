package bank.icbc.domain;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSenderImpl;

import static java.lang.String.format;

public class MailSender {
    @Autowired
    private JavaMailSenderImpl mailSender;

    public void sendEmailAfterUserRegistration(String nickname, String emailAddress) {
        String subject = "Welcome!";
        String content = format("Dear %s, welcome to the bank", nickname);

        mailSender.send(buildMessage(emailAddress, subject, content));
    }

    public void sendEmailWhenUserBecomePremium(String nickname, String emailAddress) {
        String subject = "Notice";
        String content = format("%s is now a premium customer", nickname);

        mailSender.send(buildMessage(emailAddress, subject, content));
    }

    private SimpleMailMessage buildMessage(String emailAddress, String subject, String content) {
        String fromAddress = "admin@thebank.com";

        SimpleMailMessage mailMessage = new SimpleMailMessage();

        mailMessage.setTo(emailAddress);
        mailMessage.setFrom(fromAddress);
        mailMessage.setSubject(subject);
        mailMessage.setText(content);

        return mailMessage;
    }

}
