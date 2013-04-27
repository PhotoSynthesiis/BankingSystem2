package bank.icbc.util;

import bank.icbc.domain.CustomMailMessage;
import bank.icbc.domain.Customer;

import static java.lang.String.format;

public class EmailMessageBuilder {
    private EmailMessageBuilder() {
    }

    public static CustomMailMessage buildEmailMessageSendToCustomerAfterRegistration(Customer customer) {
        String fromAddress = "admin@thebank.com";
        String subject = "Welcome!";
        String content = "Dear " + customer.getNickname() + ", welcome to the bank";
        String toAddress = customer.getEmailAddress();

        return buildMessage(fromAddress, toAddress, subject, content);
    }

    public static CustomMailMessage buildEmailMessageSendToManagerAfterCustomerBecomePremium(String nickname) {
        String subject = "Notice";
        String content = format("%s is now a premium customer", nickname);
        String toAddress = "qsli@thoughtworks.com";
        String fromAddress = "admin@thebank.com";

        return buildMessage(fromAddress, toAddress, subject, content);
    }

    private static CustomMailMessage buildMessage(String fromAddress, String toAddress, String subject, String content) {
        CustomMailMessage mailMessage = new CustomMailMessage();
        mailMessage.setToAddress(toAddress);
        mailMessage.setFromAddress(fromAddress);
        mailMessage.setEmailSubject(subject);
        mailMessage.setEmailContent(content);

        return mailMessage;
    }
}
