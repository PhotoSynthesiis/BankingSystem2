package bank.icbc.util;

import bank.icbc.domain.Customer;
import org.springframework.mail.SimpleMailMessage;

import static java.lang.String.format;

public class EmailMessageGenerator {
    private EmailMessageGenerator() {
    }

    public static SimpleMailMessage buildEmailMessageSendToCustomerAfterRegistration(Customer customer) {
        String fromAddress = "admin@thebank.com";
        String subject = "Welcome!";
        String content = "Dear " + customer.getNickname() + ", welcome to the bank";
        String toAddress = customer.getEmailAddress();

        return buildMessage(fromAddress, toAddress, subject, content);
    }

    public static SimpleMailMessage buildEmailMessageSendToManagerAfterCustomerBecomePremium(String nickname) {
        String subject = "Notice";
        String content = format("%s is now a premium customer", nickname);
        String toAddress = "qsli@thoughtworks.com";
        String fromAddress = "admin@thebank.com";

        return buildMessage(fromAddress, toAddress, subject, content);
    }


    private static SimpleMailMessage buildMessage(String fromAddress, String emailAddress, String subject, String content) {
        SimpleMailMessage mailMessage = new SimpleMailMessage();

        mailMessage.setTo(emailAddress);
        mailMessage.setFrom(fromAddress);
        mailMessage.setSubject(subject);
        mailMessage.setText(content);

        return mailMessage;
    }
}
